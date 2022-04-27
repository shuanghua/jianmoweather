package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.model.FavoriteReturn
import dev.shuanghua.weather.data.network.ShenZhenService

class FavoriteRemoteDataSource(private val api: ShenZhenService) {

    suspend fun getFavoriteCityWeather(param: String): FavoriteReturn? {
        val response = api.getFavoriteCityWeather(param)
        if (response.isSuccessful){
            return response.body()?.data
        }
        return null
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRemoteDataSource? = null
        fun getInstance(api: ShenZhenService): FavoriteRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteRemoteDataSource(api).also { INSTANCE = it }
            }
        }
    }
}