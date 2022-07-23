package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteId(favoriteId: FavoriteId)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCityWeather(favorites: List<FavoriteCityWeather>)


    @Query("SELECT cityId FROM favorite_id")
    fun observerFavoriteId(): Flow<List<String>>

    @Delete
    suspend fun deleteFavoriteId(favoriteId: FavoriteId)

//    @Query("DELETE FROM favorite_id WHERE cityId = :cityId")
//    suspend fun deleteFavoriteId2(cityId: String)

    @Query("DELETE FROM favorite WHERE cityid = :cityId")
    suspend fun favoriteCityWeather(cityId: String)

    @Query("SELECT * FROM favorite")
    fun observerCityWeather(): Flow<List<FavoriteCityWeather>>
}