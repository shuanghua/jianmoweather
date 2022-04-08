package jianmoweather.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import jianmoweather.data.db.entity.*

/**
 * 首页的所需要的信息
 */
data class Weather(
    @Embedded val temperature: WeatherScreenEntity? = null,

    @Relation(
        entity = Alarm::class,
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val alarms: List<Alarm> = emptyList(),


    @Relation(
        entity = OneDay::class,
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneDays: List<OneDay> = emptyList(),


    @Relation(
        entity = OtherItem::class,
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val others: List<OtherItem> = emptyList(),


    @Relation(
        entity = OneHour::class,
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneHours: List<OneHour> = emptyList(),


    @Relation(
        entity = HealthExponent::class,
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val healthExponents: List<HealthExponent> = emptyList(),
)