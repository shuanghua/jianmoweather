package jianmoweather.data.repo.favorite

import cjianmoweather.data.db.entity.Favorite
import jianmoweather.data.db.dao.CityDao


class FavoriteLocalDataSource(private val cityDao: CityDao) {
    suspend fun deleteFavorite(favorite: Favorite) = cityDao.deleteFavorite(favorite)

    suspend fun insertFavorites(favorite: Favorite) = cityDao.insertFavoritesOne(favorite)

    suspend fun insertFavoritesList(favorites: MutableList<Favorite>) =
        cityDao.insertFavoritesList(favorites)

    suspend fun queryFavorites(): MutableList<Favorite> {
        return cityDao.findFavorites()
    }

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