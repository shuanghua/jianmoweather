package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.model.InnerParams
import dev.shuanghua.weather.data.android.model.asFavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.repository.convert.asWeatherEntity
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.data.android.repository.convert.asFavoriteStation
import dev.shuanghua.weather.data.android.repository.convert.asRequestModel
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.asArrayList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val networkDataSource: NetworkDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {

    /**
     * concurrency request multiple stations weather
     * 并发请求多个站点天气数据
     */
    suspend fun getStationsListWeather(paramsList: List<String>): List<FavoriteStation> {
        return withContext(dispatchers.io) {
            val networkDeferred: List<Deferred<FavoriteStation>> = paramsList.map {
                async { networkDataSource.getMainWeather(it).asFavoriteStation() }
            }
            networkDeferred.awaitAll()
        }
    }

    fun observerFavoriteStationParams(): Flow<List<FavoriteStationWeatherParams>> {
        return favoriteDao.observerFavoriteStationParam()
            .map {
                it.map(FavoriteStationWeatherParamsEntity::asRequestModel)
            }
    }

    suspend fun saveStationParam(innerParams: InnerParams, stationName: String) =
        withContext(dispatchers.io) {
            val paramsEntity: FavoriteStationWeatherParamsEntity =
                innerParams.asFavoriteStationWeatherParams().asWeatherEntity(stationName)
            favoriteDao.insertStationParam(paramsEntity)
        }

    suspend fun deleteStationParam(stationName: String) =
        withContext(dispatchers.io) {
            favoriteDao.deleteStationWeatherParam(stationName)
        }

//-------------------------------------------------------------------------------------------//

    fun observerCityIds(): Flow<ArrayList<String>> =
        favoriteDao.observerCityId().map { idList ->
            idList.asArrayList()
        }

    suspend fun getFavoriteCityWeather(params: String): List<FavoriteCity> =
        networkDataSource.getFavoriteCityWeatherList(params)
            .map(ShenZhenFavoriteCityWeather::asExternalModel)

    suspend fun deleteCity(cityId: String) =
        withContext(dispatchers.io) {
            favoriteDao.deleteCity(cityId)
        }

    suspend fun saveFavoriteCity(cityId: String) =
        withContext(dispatchers.io) {
            val favoriteCityIdEntity = FavoriteCityIdEntity(id = cityId)
            favoriteDao.insertCityId(favoriteCityIdEntity)
        }
}
