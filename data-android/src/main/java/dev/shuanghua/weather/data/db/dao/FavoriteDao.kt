package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.db.entity.FavoriteStationParamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStationParam(stationParam: FavoriteStationParamEntity)

    @Query("SELECT * FROM station_param")
    fun observerStationParam(): Flow<List<FavoriteStationParamEntity>>

    @Query("DELETE FROM station_param WHERE stationName = :stationName")
    suspend fun deleteFavoriteStationParam(stationName: String)

//  ------------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: FavoriteCityEntity)

    @Query("SELECT cityId FROM favorite_city")
    fun observerCityIds(): Flow<List<String>>

    @Query("DELETE FROM favorite_city WHERE cityId = :cityId")
    fun deleteCity(cityId: String)
}