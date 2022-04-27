package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.model.FavoriteReturn

class FavoriteRepository(private val remoteDataSource: FavoriteRemoteDataSource) {

    suspend fun getFavoriteCityWeather(param:String): FavoriteReturn? {
        return remoteDataSource.getFavoriteCityWeather(param)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            dataSource: FavoriteRemoteDataSource
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) { INSTANCE ?: FavoriteRepository(dataSource).also { INSTANCE = it } }
        }
    }
}