package jianmoweather.data.db.dao

import androidx.room.*
import jianmoweather.data.db.entity.*
import jianmoweather.data.db.pojo.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemperature(temperature: WeatherScreenEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarms(list: List<Alarm>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherItems(others: List<OtherItem>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthExponent(healthExponents: List<HealthExponent>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneHours(list: List<OneHour>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneDays(list: List<OneDay>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHalfHours(halfHours: List<HalfHour>)

    //---------------------------------------Query--------------------------------------------------
//    @Transaction
//    @Query("SELECT * FROM Temperature WHERE cityId = :cityId")
//    fun findWeather(cityId: String): Flow<Weather>


    @Transaction
    @Query("SELECT * FROM WeatherScreenEntity WHERE cityId = :cityId")
    fun findTemperature(cityId: String): Flow<WeatherScreenEntity>

    @Transaction
    @Query("SELECT * FROM Alarm WHERE _cityId = :cityId")
    fun findAlarms(cityId: String): Flow<List<Alarm>>

    @Transaction
    @Query("SELECT * FROM OtherItem WHERE _cityId = :cityId")
    fun findOtherItems(cityId: String): Flow<List<OtherItem>>

    @Transaction
    @Query("SELECT * FROM HealthExponent WHERE _cityId = :cityId")
    fun findHealthExponents(cityId: String): Flow<List<HealthExponent>>

    @Transaction
    @Query("SELECT * FROM OneDay WHERE _cityId = :cityId")
    fun findOneDays(cityId: String): Flow<List<OneDay>>

    @Transaction
    @Query("SELECT * FROM OneHour WHERE _cityId = :cityId")
    fun findOneHours(cityId: String): Flow<List<OneHour>>


    //----------------------------------------------------------------------------------------------
//    @Transaction
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWeather(weather: Weather)

    @Transaction
    @Query("SELECT * FROM WeatherScreenEntity WHERE screen = :screen")
    fun observerWeather(screen: String): Flow<Weather?>
}