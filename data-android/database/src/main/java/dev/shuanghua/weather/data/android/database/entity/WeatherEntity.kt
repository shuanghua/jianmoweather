package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 此文件是天气首页包含对应的 Entity 数据
 *
 * 当前温度及描述
 */
@Entity(
    tableName = "weather",
    indices = [Index(
        value = ["cityId"], // 将 cityId 设置为唯一索引
        unique = true
    )]
)
data class WeatherEntity(
    @PrimaryKey val screen: String = "WeatherScreen",
    @ColumnInfo(defaultValue = "") val cityId: String,
    @ColumnInfo(defaultValue = "") val cityName: String,
    @ColumnInfo(defaultValue = "") val temperature: String,
    @ColumnInfo(defaultValue = "") val description: String,
    @ColumnInfo(defaultValue = "") val airQuality: String,
    @ColumnInfo(defaultValue = "") val airQualityIcon: String,
    @ColumnInfo(defaultValue = "") val lunarCalendar: String,
    @ColumnInfo(defaultValue = "") val stationName: String,
    @ColumnInfo(defaultValue = "") val stationId: String,
    @ColumnInfo(defaultValue = "") val locationStationId: String,
    @ColumnInfo(defaultValue = "") val sunUp: String,
    @ColumnInfo(defaultValue = "") val sunDown: String,
)