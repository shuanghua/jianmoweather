package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.data.db.pojo.PackingWeather
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WeatherDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWeather(
        temperature: WeatherEntity,
        listAlarm: List<AlarmIconEntity>,
        listOneDay: List<OneDayEntity>,
        listCondition: List<ConditionEntity>,
        listOnHour: List<OneHourEntity>,
        listExponent: List<ExponentEntity>
    )

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTemperature(temperature: WeatherEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAlarms(list: List<AlarmIconEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOtherItems(others: List<ConditionEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertHealthExponent(healthExponents: List<ExponentEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOneHours(list: List<OneHourEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOneDays(list: List<OneDayEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertHalfHours(halfHours: List<HalfHourEntity>)

    //---------------------------------------Query--------------------------------------------------
//    @Transaction
//    @Query("SELECT * FROM Temperature WHERE cityId = :cityId")
//    fun findWeather(cityId: String): Flow<Weather>


    @Transaction
    @Query("SELECT * FROM weather WHERE cityId = :cityId")
    abstract fun findWeather(cityId: String): Flow<WeatherEntity>

    @Transaction
    @Query("SELECT * FROM alarm WHERE _cityId = :cityId")
    abstract fun findAlarms(cityId: String): Flow<List<AlarmIconEntity>>

    @Transaction
    @Query("SELECT * FROM condition WHERE _cityId = :cityId")
    abstract fun findOtherItems(cityId: String): Flow<List<ConditionEntity>>

    @Transaction
    @Query("SELECT * FROM exponent WHERE _cityId = :cityId")
    abstract fun findHealthExponents(cityId: String): Flow<List<ExponentEntity>>

    @Transaction
    @Query("SELECT * FROM one_day WHERE _cityId = :cityId")
    abstract fun findOneDays(cityId: String): Flow<List<OneDayEntity>>

    @Transaction
    @Query("SELECT * FROM one_hour WHERE _cityId = :cityId")
    abstract fun findOneHours(cityId: String): Flow<List<OneHourEntity>>


    //----------------------------------------------------------------------------------------------

    @Transaction
    @Query("SELECT * FROM weather")
    abstract fun getWeather(): Flow<PackingWeather?>
}