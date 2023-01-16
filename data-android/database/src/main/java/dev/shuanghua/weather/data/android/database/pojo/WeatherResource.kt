package dev.shuanghua.weather.data.android.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import dev.shuanghua.weather.data.android.database.entity.AlarmIconEntity
import dev.shuanghua.weather.data.android.database.entity.ConditionEntity
import dev.shuanghua.weather.data.android.database.entity.ExponentEntity
import dev.shuanghua.weather.data.android.database.entity.OneDayEntity
import dev.shuanghua.weather.data.android.database.entity.OneHourEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherEntity
import dev.shuanghua.weather.data.android.database.entity.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather

/**
 * 首页的所需要的信息,
 * 此数据类称为 pojo，通常用来存放从数据库读取的多表数据
 * 普通bean类: 需要通过 setter来设置,所以需要公开的 val
 *
 * Embedded : 用于对象
 * Relation : 用于集合
 */
data class WeatherResource(
    @Embedded
    val weatherEntity: WeatherEntity,

    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val alarmIcons: List<AlarmIconEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneDays: List<OneDayEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val conditions: List<ConditionEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneHours: List<OneHourEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val exponents: List<ExponentEntity>,
)

/**
 * 将数据库查询出的数据转换成 ui Model
 * 通常在 Repository 中调用
 */
fun WeatherResource.asExternalModel(): Weather = Weather(
    cityId = weatherEntity.cityId,
    cityName = weatherEntity.cityName,
    temperature = weatherEntity.temperature,
    description = weatherEntity.description,
    airQuality = weatherEntity.airQuality,
    airQualityIcon = weatherEntity.airQuality,
    lunarCalendar = weatherEntity.lunarCalendar,
    stationName = weatherEntity.stationName,
    stationId = weatherEntity.stationId,
    locationStationId = weatherEntity.locationStationId,
    sunUp = weatherEntity.sunUp,
    sunDown = weatherEntity.sunDown,

    alarmIcons = alarmIcons.map(AlarmIconEntity::asExternalModel),
    oneDays = oneDays.map(OneDayEntity::asExternalModel),
    oneHours = oneHours.map(OneHourEntity::asExternalModel),
    conditions = conditions.map(ConditionEntity::asExternalModel),
    exponents = exponents.map(ExponentEntity::asExternalModel)
)