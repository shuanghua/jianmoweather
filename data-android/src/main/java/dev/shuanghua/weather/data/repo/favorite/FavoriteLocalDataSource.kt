package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.dao.CityDao


class FavoriteLocalDataSource(private val cityDao: CityDao) {

    companion object {
        @Volatile
        private var INSTANCE: FavoriteLocalDataSource? = null

        fun getInstance(cityDao: CityDao): FavoriteLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteLocalDataSource(cityDao).also { INSTANCE = it }
            }
        }
    }
}