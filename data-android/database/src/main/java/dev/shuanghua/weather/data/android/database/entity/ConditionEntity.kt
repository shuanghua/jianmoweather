package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.Condition

/**
 * 湿度，气压...
 */
@Entity(
    tableName = "condition",
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class ConditionEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(defaultValue = "", name = "_cityId") val cityId: String,
    @ColumnInfo(defaultValue = "")val name: String,
    @ColumnInfo(defaultValue = "")val value: String,
)

fun ConditionEntity.asExternalModel() = Condition(
    id = id,
    cityId = cityId,
    name = name,
    value = value
)
