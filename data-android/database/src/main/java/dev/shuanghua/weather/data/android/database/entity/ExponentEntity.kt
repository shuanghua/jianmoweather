package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.Exponent

/**
 * 健康指数
 */
@Entity(
    tableName = "exponent",
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class ExponentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(defaultValue = "", name = "_cityId") val cityId: String,
    @ColumnInfo(defaultValue = "") val title: String,
    @ColumnInfo(defaultValue = "") val level: String,
    @ColumnInfo(defaultValue = "") val levelDesc: String,
    @ColumnInfo(defaultValue = "") val levelAdvice: String,
)

fun ExponentEntity.asExternalModel() = Exponent(
    id = id,
    cityId = cityId,
    title = title,
    level = level,
    levelDesc = levelDesc,
    levelAdvice = levelAdvice
)