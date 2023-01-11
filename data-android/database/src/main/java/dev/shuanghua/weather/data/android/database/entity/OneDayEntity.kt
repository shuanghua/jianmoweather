package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.OneDay

/**
 * 每天天气
 */
@Entity(
    tableName = "one_day",
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class OneDayEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(defaultValue = "", name = "_cityId") val cityId: String,
    @ColumnInfo(defaultValue = "") val date: String,//日期
    @ColumnInfo(defaultValue = "") val week: String,//今天
    @ColumnInfo(defaultValue = "") val desc: String,//描述
    @ColumnInfo(defaultValue = "") val t: String,     //天气范围
    @ColumnInfo(defaultValue = "") val minT: String,//最低温度
    @ColumnInfo(defaultValue = "") val maxT: String,//最高温度
    @ColumnInfo(defaultValue = "") val iconName: String,//相应天气 icon 名字
)

fun OneDayEntity.asExternalModel() = OneDay(
    id = id,
    cityId = cityId,
    date = date,
    week = week,
    desc = desc,
    t = t,
    minT = minT,
    maxT = maxT,
    iconName = iconName
)