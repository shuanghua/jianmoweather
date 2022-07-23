package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteId(favoriteId: FavoriteId)


    @Query("SELECT cityId FROM favorite_id")
    fun observerFavoriteId(): Flow<List<String>>

    @Delete
    suspend fun deleteFavoriteId(favoriteId: FavoriteId)
}