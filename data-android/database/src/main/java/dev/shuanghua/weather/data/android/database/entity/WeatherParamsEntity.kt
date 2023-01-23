package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_station_weather_param"
)
data class WeatherParamsEntity(
    @PrimaryKey
    val stationName: String, //主键
    val lon: String = "",
    val lat: String = "",
    val isAuto: String = "1",
    val cityIds: String = "",
    val cityId: String = "",
    val obtId: String = "",
    val cityName: String = "",
    val district: String = ""
)


