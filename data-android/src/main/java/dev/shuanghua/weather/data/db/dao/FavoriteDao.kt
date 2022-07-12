package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<Favorite>)

    @Query(
        "SELECT * FROM favorite ORDER BY" +
                " CASE WHEN :isAsc = 0 THEN position END ASC, " +
                " CASE WHEN :isAsc = 1 THEN position END DESC "
    )
    fun observerFavorites(isAsc: Int = 0): Flow<List<Favorite>>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}