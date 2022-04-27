package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Transaction
    @Insert
    suspend fun insertFavorites(favorites: List<Favorite>)

    @Query("SELECT * FROM favorite")
    fun observerFavorites(): Flow<List<Favorite>>
}