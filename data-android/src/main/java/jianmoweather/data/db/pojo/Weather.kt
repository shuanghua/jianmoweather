package jianmoweather.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import jianmoweather.data.db.entity.*

/**
 * 首页的所需要的信息
 * 普通类: 需要通过 setter来设置,所以需要公开的 val
 * data class
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

//	override fun equals(other: Any?): Boolean = when {
//		other === this -> true
//		other is Weather -> {
//			temperature == other.temperature &&
//					alarms == other.alarms &&
//					oneDays == other.oneDays &&
//					others == other.others &&
//					oneHours == other.oneHours &&
//					healthExponents == other.healthExponents
//
//		}
//		else -> false
//	}
//
//	override fun hashCode(): Int =
//		Objects.hash(
//			temperature,
//			alarms,
//			oneDays,
//			others,
//			oneHours,
//			healthExponents
//		)
)