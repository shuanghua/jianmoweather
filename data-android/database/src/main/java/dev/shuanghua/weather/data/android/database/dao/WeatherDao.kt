package dev.shuanghua.weather.data.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shuanghua.weather.data.android.database.entity.AlarmIconEntity
import dev.shuanghua.weather.data.android.database.entity.ConditionEntity
import dev.shuanghua.weather.data.android.database.entity.ExponentEntity
import dev.shuanghua.weather.data.android.database.entity.HalfHourEntity
import dev.shuanghua.weather.data.android.database.entity.OneDayEntity
import dev.shuanghua.weather.data.android.database.entity.OneHourEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherEntities
import dev.shuanghua.weather.data.android.database.entity.WeatherEntity
import dev.shuanghua.weather.data.android.database.pojo.WeatherResource
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WeatherDao {

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertWeather(
		weatherEntity: WeatherEntity,
		alarmEntities: List<AlarmIconEntity>,
		oneDayEntities: List<OneDayEntity>,
		conditionEntities: List<ConditionEntity>,
		onHourEntities: List<OneHourEntity>,
		exponentEntities: List<ExponentEntity>,
	)

	suspend fun insertWeatherEntities(weatherEntities: WeatherEntities) {
		weatherEntities.apply {
			insertWeather(
				weatherEntity = weatherEntity,
				alarmEntities = alarmEntities,
				oneDayEntities = oneDayEntities,
				onHourEntities = onHourEntities,
				conditionEntities = conditionEntities,
				exponentEntities = exponentEntities,
			)
		}
	}


	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertTemperature(weatherEntity: WeatherEntity)

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

	/**
	 * 返回值包括带有 @Relation 的 POJO。
	 * 通常希望使用@Transaction 注释此方法
	 * 以避免 POJO 及其关系之间可能出现不一致的结果
	 */
	@Transaction
	@Query("SELECT * FROM weather")
	abstract fun getWeather(): Flow<WeatherResource?>
}