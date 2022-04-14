package jianmoweather.data.db.dao

import androidx.room.*
import jianmoweather.data.db.entity.*
import jianmoweather.data.db.pojo.Weather
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WeatherDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertWeather(
        temperature: Temperature,
        listAlarm: List<Alarm>,
        listOneDay: List<OneDay>,
        listCondition: List<Condition>,
        listOnHour: List<OneHour>,
        listExponent: List<Exponent>
    )

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTemperature(temperature: Temperature)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAlarms(list: List<Alarm>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOtherItems(others: List<Condition>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertHealthExponent(healthExponents: List<Exponent>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOneHours(list: List<OneHour>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOneDays(list: List<OneDay>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertHalfHours(halfHours: List<HalfHour>)

    //---------------------------------------Query--------------------------------------------------
//    @Transaction
//    @Query("SELECT * FROM Temperature WHERE cityId = :cityId")
//    fun findWeather(cityId: String): Flow<Weather>


    @Transaction
    @Query("SELECT * FROM Temperature WHERE cityId = :cityId")
    abstract fun findTemperature(cityId: String): Flow<Temperature>

    @Transaction
    @Query("SELECT * FROM Alarm WHERE _cityId = :cityId")
    abstract fun findAlarms(cityId: String): Flow<List<Alarm>>

    @Transaction
    @Query("SELECT * FROM Condition WHERE _cityId = :cityId")
    abstract fun findOtherItems(cityId: String): Flow<List<Condition>>

    @Transaction
    @Query("SELECT * FROM Exponent WHERE _cityId = :cityId")
    abstract fun findHealthExponents(cityId: String): Flow<List<Exponent>>

    @Transaction
    @Query("SELECT * FROM OneDay WHERE _cityId = :cityId")
    abstract fun findOneDays(cityId: String): Flow<List<OneDay>>

    @Transaction
    @Query("SELECT * FROM OneHour WHERE _cityId = :cityId")
    abstract fun findOneHours(cityId: String): Flow<List<OneHour>>


    //----------------------------------------------------------------------------------------------
//    @Transaction
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWeather(weather: Weather)

    @Transaction
    @Query("SELECT * FROM Temperature WHERE screen = :screen")
    abstract fun observerWeather(screen: String): Flow<Weather?>
}