package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.HalfHour

@Entity(
    tableName = "half_hour",
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherEntity::class,//entity：指定父表所对应的类
        parentColumns = ["cityId"],//一般的，子表外键都设置为对应为父表的主键，
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class HalfHourEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "_cityId") val cityId: String,
    val hour: String,//未来半小时时间
    val t: String,//未来半个小时天气
)

fun HalfHourEntity.asExternalModel() = HalfHour(
    id = id,
    cityId = cityId,
    hour = hour,
    t = t
)