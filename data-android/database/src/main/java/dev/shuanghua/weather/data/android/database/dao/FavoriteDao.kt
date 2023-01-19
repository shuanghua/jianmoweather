package dev.shuanghua.weather.data.android.database.dao

import androidx.room.*
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStationParam(stationParam: FavoriteStationWeatherParamsEntity)


    /**
     * 目前这里观察的目的只是使用其的 stationName
     * TODO 支持收藏页面站点的温度显示
     */
    @Query("SELECT * FROM favorite_station_weather_param")
    fun observerFavoriteStationParam(): Flow<List<FavoriteStationWeatherParamsEntity>>


    @Query("DELETE FROM favorite_station_weather_param WHERE stationName = :stationName")
    suspend fun deleteStationWeatherParam(stationName: String)


    @Query("SELECT * FROM favorite_station_weather_param WHERE stationName = :stationName")
    suspend fun getStationWeatherParams(stationName: String): FavoriteStationWeatherParamsEntity



    //  ---------------------------------City---------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityId(city: FavoriteCityIdEntity)

    @Query("SELECT id FROM favorite_city_id")
    fun observerCityId(): Flow<List<String>>

    @Query("DELETE FROM favorite_city_id WHERE id = :cityId")
    suspend fun deleteCity(cityId: String)
}