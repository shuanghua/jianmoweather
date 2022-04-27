package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.network.ShenZhenService

class FavoriteRemoteDataSource(private val api: ShenZhenService) {

    suspend fun getFavoriteCityWeather(param: String): List<Favorite> {
        val response = api.getFavoriteCityWeather(param)
        if (response.isSuccessful) {
            val list = response.body()?.data?.list
            if (list != null) return list
        }
        return emptyList()
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