package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.data.network.ShenZhenService
import timber.log.Timber

class FavoriteRepository(
    private val service: ShenZhenService,
    private val favoriteDao: FavoriteDao,
) {

//    fun getFavoriteCityWeather(param: String): List<FavoriteCityWeather> {
//        return service.getFavoriteCityWeather(param).body()?.data?.list!!
//    }

    suspend fun getFavoriteCityWeather(param: String): List<FavoriteCityWeather> {
        Timber.d("okhtt-->:$param")
        return service.getFavoriteCityWeather(param).body()?.data?.list!!
    }


    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            service: ShenZhenService,
            favoriteDao: FavoriteDao
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteRepository(service, favoriteDao).also { INSTANCE = it }
            }
        }
    }
}