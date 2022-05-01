package dev.shuanghua.weather.data.repo.favorite

import dev.shuanghua.weather.data.db.entity.Favorite
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val remoteDataSource: FavoriteRemoteDataSource,
    private val localDataSource: FavoriteLocalDataSource
) {

    fun observerFavoriteCityWeather(): Flow<List<Favorite>> {
        return localDataSource.observerFavoriteCityWeather()
    }

    suspend fun updateFormNetwork(param: String) {
        val favoriteList: List<Favorite> = remoteDataSource.getFavoriteCityWeather(param)
        if (favoriteList.isNotEmpty()) localDataSource.saveFavorites(favoriteList)
    }

    suspend fun deleteFavorite(favorite: Favorite) = localDataSource.deleteFavorite(favorite)
    suspend fun addFavorite(favorite: List<Favorite>) = localDataSource.saveFavorites(favorite)


    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            remoteDataSource: FavoriteRemoteDataSource,
            localDataSource: FavoriteLocalDataSource
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteRepository(
                    remoteDataSource,
                    localDataSource
                ).also { INSTANCE = it }
            }
        }
    }
}