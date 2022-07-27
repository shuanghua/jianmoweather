package dev.shuanghua.weather.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import dev.shuanghua.weather.data.db.entity.*

/**
 * 首页的所需要的信息
 * 普通类: 需要通过 setter来设置,所以需要公开的 val
 * data class
 * Embedded : 用于对象
 * Relation : 用于集合
 */
data class Weather(
	@Embedded
	val temperature: Temperature? = null,

	@Relation(
		entityColumn = "_cityId",
		parentColumn = "cityId"
	)
	val alarms: List<Alarm> = emptyList(),


	@Relation(
		entityColumn = "_cityId",
		parentColumn = "cityId"
	)
	val oneDays: List<OneDay> = emptyList(),


	@Relation(
		entityColumn = "_cityId",
		parentColumn = "cityId"
	)
	val others: List<Condition> = emptyList(),


	@Relation(
		entityColumn = "_cityId",
		parentColumn = "cityId"
	)
	val oneHours: List<OneHour> = emptyList(),


	@Relation(
		entityColumn = "_cityId",
		parentColumn = "cityId"
	)
	val exponents: List<Exponent> = emptyList()
)