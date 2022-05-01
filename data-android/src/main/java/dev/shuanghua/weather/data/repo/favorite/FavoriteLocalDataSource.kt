package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.Favorite
import kotlinx.coroutines.flow.Flow


class FavoriteLocalDataSource(private val favoriteDao: FavoriteDao) {

    suspend fun saveFavorites(favorites: List<Favorite>) = favoriteDao.insertFavorites(favorites)
    fun observerFavoriteCityWeather(): Flow<List<Favorite>> = favoriteDao.observerFavorites()

    suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.deleteFavorite(favorite)

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