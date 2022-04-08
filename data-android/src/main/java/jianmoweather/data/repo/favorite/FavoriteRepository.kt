package jianmoweather.data.repo.favorite

import cjianmoweather.data.db.entity.Favorite

class FavoriteRepository(private val dataSource: FavoriteLocalDataSource) {

    suspend fun queryFavorites(): MutableList<Favorite> = dataSource.queryFavorites()//LiveData


    suspend fun deleteFavorite(favorite: Favorite) = dataSource.deleteFavorite(favorite)

    suspend fun insertFavoritesList(favorites: MutableList<Favorite>) =
        dataSource.insertFavoritesList(favorites)

    suspend fun insertFavorites(favorite: Favorite) {
        dataSource.insertFavorites(favorite)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            dataSource: FavoriteLocalDataSource
        ): FavoriteRepository {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: FavoriteRepository(
                            dataSource
                        ).also { INSTANCE = it }
                }
        }
    }
}