package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteStationParamEntity
import dev.shuanghua.weather.data.db.entity.asFavoriteStationModel
import dev.shuanghua.weather.data.model.FavoriteCityWeather
import dev.shuanghua.weather.data.model.FavoriteStationResource
import dev.shuanghua.weather.data.network.ShenZhenWeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(
    private val service: ShenZhenWeatherApi,
    private val favoriteDao: FavoriteDao,
) {

    suspend fun insertStationParam(stationParam: FavoriteStationParamEntity) {
        favoriteDao.insertStationParam(stationParam)
    }

    fun observerStationParam(): Flow<List<FavoriteStationResource>> {
        return favoriteDao.observerStationParam().map { entities ->
            entities.map { it.asFavoriteStationModel() }
        }
    }

    suspend fun deleteStationParam(stationName: String) {
        favoriteDao.deleteFavoriteStationParam(stationName)
    }

    fun observerCityIds(): Flow<List<String>> = favoriteDao.observerCityIds()

    suspend fun deleteCity(cityId: String) {
        favoriteDao.deleteCity(cityId)
    }

    suspend fun getCityWeather(param: String): List<FavoriteCityWeather> =
        service.getFavoriteCityWeather(param).body()?.data?.list!!

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            service: ShenZhenWeatherApi,
            favoriteDao: FavoriteDao,
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteRepository(service, favoriteDao).also { INSTANCE = it }
            }
        }
    }
}
