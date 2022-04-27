package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.Favorite


class FavoriteLocalDataSource(private val favoriteDao: FavoriteDao) {

    suspend fun saveFavorites(favorites: List<Favorite>) = favoriteDao.insertFavorites(favorites)

    companion object {
        @Volatile
        private var INSTANCE: FavoriteLocalDataSource? = null

        fun getInstance(favoriteDao: FavoriteDao): FavoriteLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteLocalDataSource(favoriteDao).also { INSTANCE = it }
            }
        }
    }
}