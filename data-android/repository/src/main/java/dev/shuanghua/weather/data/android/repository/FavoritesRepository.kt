package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.NetworkModel
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.repository.converter.asEntity
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.data.android.repository.converter.asFavoriteStation
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.asArrayList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface FavoritesRepository {
	suspend fun getFavoritesStationsList(paramsList: List<WeatherParams>): List<FavoriteStation>
	suspend fun getFavoritesWeather(params: WeatherParams): Weather
	fun observerFavoriteStationParams(): Flow<List<WeatherParams>>
	suspend fun saveStationParam(weatherParams: WeatherParams, stationName: String)
	suspend fun deleteStationParam(stationName: String)
	suspend fun getStationParamsByName(stationName: String): WeatherParams
	fun observerCityIds(): Flow<ArrayList<String>>
	suspend fun getFavoriteCityWeather(params: FavoriteCityParams): List<FavoriteCity>
	suspend fun deleteCity(cityId: String)
	suspend fun saveFavoriteCity(cityId: String)
}

class FavoritesRepositoryImpl(
	private val networkDataSource: NetworkDataSource,
	private val favoriteDao: FavoriteDao,
	private val dispatchers: AppDispatchers,
) : FavoritesRepository {

	/**
	 * concurrency request multiple stations weather
	 * 并发请求多个站点天气数据
	 */
	override suspend fun getFavoritesStationsList(
		paramsList: List<WeatherParams>,
	): List<FavoriteStation> = withContext(dispatchers.io) {
		val networkDeferred: List<Deferred<FavoriteStation>> = paramsList.map {  // 生成多个 Deferred
			async { networkDataSource.getMainWeather(it).asFavoriteStation() }
		}
		networkDeferred.awaitAll()// 并发
	}


	/**
	 * 打开天气详情页，类似首页
	 */
	override suspend fun getFavoritesWeather(
		params: WeatherParams,
	): Weather = networkDataSource
		.getMainWeather(params)
		.let { NetworkModel(szw = it) }
		.asExternalModel()


	override fun observerFavoriteStationParams(): Flow<List<WeatherParams>> {
		return favoriteDao.observerFavoriteStationParam().map {
			it.map(WeatherParamsEntity::asExternalModel)
		}
	}

	override suspend fun saveStationParam(
		weatherParams: WeatherParams,
		stationName: String,
	): Unit = withContext(dispatchers.io) {
		val weatherParamsEntity = weatherParams.asEntity(stationName)
		favoriteDao.insertStationParam(weatherParamsEntity)
	}

	override suspend fun deleteStationParam(
		stationName: String,
	) = favoriteDao
		.deleteStationWeatherParam(stationName)


	override suspend fun getStationParamsByName(
		stationName: String,
	): WeatherParams = favoriteDao
		.getStationWeatherParams(stationName).asExternalModel()


//--------------City---------------------------------------------------------------------------//

	override fun observerCityIds(): Flow<ArrayList<String>> = favoriteDao
		.observerCityId().map { idList: List<String> -> idList.asArrayList() }

	override suspend fun getFavoriteCityWeather(
		params: FavoriteCityParams,
	): List<FavoriteCity> = networkDataSource.getFavoriteCityWeather(params)
		.map(ShenZhenFavoriteCityWeather::asExternalModel)

	override suspend fun deleteCity(cityId: String) {
		favoriteDao.deleteCity(cityId)
	}

	override suspend fun saveFavoriteCity(cityId: String) {
		val favoriteCityIdEntity = FavoriteCityIdEntity(id = cityId)
		favoriteDao.insertCityId(favoriteCityIdEntity)
	}
}
