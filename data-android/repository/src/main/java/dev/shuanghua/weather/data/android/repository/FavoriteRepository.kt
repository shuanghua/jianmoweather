package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.InnerParams
import dev.shuanghua.weather.data.android.model.asFavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.repository.convert.asEntity
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.asArrayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val networkDataSource: NetworkDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {
    //FavoriteCityEntity
    suspend fun insertStationParam(innerParams: InnerParams, stationName: String) {
        val paramsEntity: FavoriteStationWeatherParamsEntity =
            innerParams.asFavoriteStationWeatherParams().asEntity(stationName)
        favoriteDao.insertStationParam(paramsEntity)
    }

    fun observerStationParam(): Flow<List<FavoriteStation>> {
        return favoriteDao.observerStationWeatherParam()
            .map { entities: List<FavoriteStationWeatherParamsEntity> ->
                entities.map { it.asExternalModel() }
            }
    }

    suspend fun deleteStationParam(stationName: String) = withContext(dispatchers.io) {
        favoriteDao.deleteStationWeatherParam(stationName)
    }

//-------------------------------------------------------------------------------------------//

    fun observerCityIds(): Flow<ArrayList<String>> {
        return favoriteDao.observerCityIds().map { entityList ->
            entityList.asArrayList()
        }
    }

    fun getNetworkCityWeather(params: String): Flow<List<FavoriteCityWeather>> = flow {
        emit(networkDataSource.getFavoriteCityWeather(params)
            .map {
                it.asExternalModel()
            })
    }.flowOn(dispatchers.io)

    suspend fun deleteCity(cityId: String) = withContext(dispatchers.io) {
        favoriteDao.deleteCity(cityId)
    }

    suspend fun saveFavoriteCity(favoriteCity: FavoriteCity) = withContext(dispatchers.io) {
        favoriteDao.insertCity(favoriteCity.asEntity())
    }
}
