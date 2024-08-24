package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityWeatherEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationParamsEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.model.FavoriteStationParams
import dev.shuanghua.weather.data.android.model.FavoriteStationWeather
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.NetworkModel
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.repository.converter.asEntity
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.data.android.repository.converter.asFavoriteStationWeather
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException

interface FavoritesRepository {

	suspend fun getFavoriteCityDetailWeather(cityId: String): Weather // 点击收藏城市打开天气详情页使用
	suspend fun getFavoriteStationWeather(cityId: String, stationId: String): Weather //点击收藏站点
	suspend fun getFavoriteLocationStationWeather(
		latitude: String,
		longitude: String,
		cityName: String,
		district: String,
	): Weather // 如果站点的请求参数是自动定位，其中是没有站点id的，因此需要经纬度去请求

	suspend fun getFavoriteStationByName(stationName: String): FavoriteStationParams
	suspend fun saveFavoriteStation(favoriteStationParams: FavoriteStationParams)

	suspend fun deleteFavoriteCity(cityId: String)
	suspend fun deleteFavoriteStation(stationName: String)
	suspend fun clearAllFavoriteCitiesWeather()
	suspend fun clearAllFavoriteStationsWeather()

	fun observerFavoriteCities(): Flow<List<FavoriteCity>>
	fun observerFavoriteStations(): Flow<List<FavoriteStationParams>>
	fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeather>>
	fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>>

	suspend fun updateFavoriteStationsWeather(stationParams: List<FavoriteStationParams>)
	suspend fun updateFavoriteCitiesWeather(
		favoriteCities: List<FavoriteCity>,
		params: Map<String, String>,
	)
}

class FavoritesRepositoryImpl(
	private val networkDataSource: NetworkDataSource,
	private val favoriteDao: FavoriteDao,
	private val dispatchers: AppDispatchers,
) : FavoritesRepository {
	/**
	 * 并发请求多个站点天气数据
	 * 1 自动站点
	 * 2 手动站点
	 * params 不为空时，网络错误，数据库数据为空，返回假数据
	 * params 不为空时，网络错误，数据库数据不为空，返回数据库数据
	 */
	override suspend fun updateFavoriteStationsWeather(
		stationParams: List<FavoriteStationParams>,
	) = try {
		val networkDeferred: List<Deferred<FavoriteStationWeather>> = stationParams
			.map { stationParam: FavoriteStationParams ->
				if (stationParam.isAutoLocation == "1") { // 定位站点 使用首页请求定位城市的请求参数
					getStationWeatherByLocation(
						latitude = stationParam.latitude,
						longitude = stationParam.longitude,
						cityName = stationParam.cityName,
						district = stationParam.district
					)
				} else {
					getStationWeatherByStationId(
						cityId = stationParam.cityId,
						stationId = stationParam.stationId
					)
				}
			}
		val result: List<FavoriteStationWeather> = networkDeferred.awaitAll()
		favoriteDao.insertFavoriteStationsWeather(result.map(FavoriteStationWeather::asEntity))
	} catch (e: Exception) {
		val fakeData = createStationWeatherEntitiesFakeData(stationParams)
		favoriteDao.insertFavoriteStationsWeather(fakeData)
		when (e) {
			is UnknownHostException -> throw UnknownHostException("站点：请检查网络或者使用中国地区VPN")
			else -> throw e
		}
	}

	private suspend fun getStationWeatherByStationId(
		cityId: String,
		stationId: String,
	) = withContext(dispatchers.io) {
		async {
			networkDataSource.getCityWeatherByStationId(
				cityId = cityId,
				stationId = stationId
			).asFavoriteStationWeather()
		}
	}

	private suspend fun getStationWeatherByLocation(
		latitude: String,
		longitude: String,
		cityName: String,
		district: String,
	) = withContext(dispatchers.io) {
		async {
			networkDataSource.getCityWeatherByLocation(
				latitude = latitude,
				longitude = longitude,
				cityName = cityName,
				district = district
			).asFavoriteStationWeather()
		}
	}

	/**
	 * 点击 收藏城市 打开天气详情页
	 * 不保存到数据库
	 */
	override suspend fun getFavoriteCityDetailWeather(
		cityId: String,
	): Weather = NetworkModel(
		networkDataSource.getCityWeatherByCityId(cityId)
	).asExternalModel()

	override suspend fun getFavoriteStationWeather(
		cityId: String,
		stationId: String,
	): Weather = NetworkModel(
		networkDataSource.getCityWeatherByStationId(cityId, stationId)
	).asExternalModel()

	override suspend fun getFavoriteLocationStationWeather(
		latitude: String,
		longitude: String,
		cityName: String,
		district: String,
	): Weather = NetworkModel(
		networkDataSource.getCityWeatherByLocation(
			latitude,
			longitude,
			cityName,
			district
		)
	).asExternalModel()

	override fun observerFavoriteStations(): Flow<List<FavoriteStationParams>> {
		return favoriteDao.observerFavoriteStations().map {
			it.map(FavoriteStationParamsEntity::asExternalModel)
		}
	}

	override suspend fun saveFavoriteStation(
		favoriteStationParams: FavoriteStationParams,
	) {
		Timber.d("SaveStationToFavoriteUseCase 保存站点:: $favoriteStationParams")
		favoriteDao.insertFavoriteStation(favoriteStationParams.asEntity())
	}

	override suspend fun getFavoriteStationByName(
		stationName: String,
	): FavoriteStationParams = favoriteDao.getFavoriteStationByName(stationName).asExternalModel()


//--------------City---------------------------------------------------------------------------//

	override fun observerFavoriteCities(): Flow<List<FavoriteCity>> {
		return favoriteDao.observerFavoriteCities()
			.map { it.map(FavoriteCityEntity::asExternalModel) }
	}

	override fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeather>> {
		return favoriteDao.observerFavoriteCitiesWeather()
			.map { it.map(FavoriteCityWeatherEntity::asExternalModel) }
	}

	override fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>> {
		return favoriteDao.observerFavoriteStationsWeather()
			.map { it.map(FavoriteStationWeatherEntity::asExternalModel) }
	}

	/**
	 * 更新所有城市天气
	 * 对于收藏城市接口，只需要拼接id参数，请求一次即可
	 * 当没有网络时，添加城市到收藏页面没有数据（增加交互体验）
	 * 之前已收藏的城市天气数据也会被重置为默认数据
	 */
	override suspend fun updateFavoriteCitiesWeather(
		favoriteCities: List<FavoriteCity>,
		params: Map<String, String>,
	) {
		try {
			val citiesWeathers = networkDataSource // 请求网络天气数据
				.getFavoriteCityWeather(params).map(ShenZhenFavoriteCityWeather::asExternalModel)
			val cityWeatherEntities = citiesWeathers.map(FavoriteCityWeather::asEntity)// 转换为 Entity
			favoriteDao.insertFavoriteCitiesWeather(cityWeatherEntities) // 保存到数据库
		} catch (e: Exception) {
			favoriteDao.insertFavoriteCitiesWeather(createCityWeatherEntitiesFakeData(favoriteCities))
			throw Exception("城市：请检查网络或者使用中国地区VPN") // 将错误传递到 ui 显示
		}
	}

	override suspend fun deleteFavoriteCity(cityId: String) {
		favoriteDao.deleteCityAndWeather(cityId)
	}

	override suspend fun deleteFavoriteStation(stationName: String) {
		favoriteDao.deleteFavoriteStationAndWeather(stationName)
	}

	override suspend fun clearAllFavoriteCitiesWeather() {
		favoriteDao.deleteAllCitiesWeather()
	}

	override suspend fun clearAllFavoriteStationsWeather() {
		favoriteDao.deleteAllStationsWeather()
	}

	private fun createCityWeatherEntitiesFakeData(
		favoriteCities: List<FavoriteCity>,
	): List<FavoriteCityWeatherEntity> {
		return favoriteCities.map {
			FavoriteCityWeatherEntity(
				cityName = it.cityName,
				cityId = it.cityId,
				provinceName = it.provinceName,
				isAutoLocation = "",
				currentT = "",
				bgImageNew = "",
				iconUrl = "",
			)
		}
	}

	private fun createStationWeatherEntitiesFakeData(
		favoriteStations: List<FavoriteStationParams>,
	): List<FavoriteStationWeatherEntity> {
		return favoriteStations.map {
			FavoriteStationWeatherEntity(
				stationName = it.stationName,
				cityId = it.cityId,
				temperature = "",
				weatherStatus = "",
				weatherIcon = "",
				rangeT = "",
			)
		}
	}
}
