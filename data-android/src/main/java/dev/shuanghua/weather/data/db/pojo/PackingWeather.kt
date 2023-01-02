package dev.shuanghua.weather.data.db.pojo

import androidx.room.Embedded
import androidx.room.Relation
import dev.shuanghua.weather.data.db.entity.AlarmIconEntity
import dev.shuanghua.weather.data.db.entity.ConditionEntity
import dev.shuanghua.weather.data.db.entity.ExponentEntity
import dev.shuanghua.weather.data.db.entity.OneDayEntity
import dev.shuanghua.weather.data.db.entity.OneHourEntity
import dev.shuanghua.weather.data.db.entity.WeatherEntity
import dev.shuanghua.weather.data.db.entity.asExternalModel
import dev.shuanghua.weather.data.model.WeatherResource

/**
 * 首页的所需要的信息,此数据类称为 pojo，通常用来存放从数据库读取的多表数据,不应该用于界面显示
 * 普通bean类: 需要通过 setter来设置,所以需要公开的 val
 * data class
 * Embedded : 用于对象
 * Relation : 用于集合
 */
data class PackingWeather(
    @Embedded
    val weatherEntity: WeatherEntity,

    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val alarmIcons: List<AlarmIconEntity> = emptyList(),


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
 * 将数据库查询出的数据转换成通用Model
 */
fun PackingWeather.asExternalModel() = WeatherResource(
    screen = weatherEntity.screen,
    cityId = weatherEntity.cityId,
    cityName = weatherEntity.cityName,
    temperature = weatherEntity.temperature,
    description = weatherEntity.description,
    airQuality = weatherEntity.airQuality,
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