package dev.shuanghua.weather.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.model.OneDay
import dev.shuanghua.weather.data.model.OneHour

/**
 * 每小时天气
 */
@Entity(
    tableName = "one_hour",
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class OneHourEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(defaultValue = "", name = "_cityId") val cityId: String,
    @ColumnInfo(defaultValue = "")val hour: String,
    @ColumnInfo(defaultValue = "")val t: String,
    @ColumnInfo(defaultValue = "")val icon: String,
    @ColumnInfo(defaultValue = "")val rain: String,
)

fun OneHourEntity.asExternalModel() = OneHour(
    id = id,
    cityId = cityId,
    hour = hour,
    t = t,
    icon = icon,
    rain = rain

)