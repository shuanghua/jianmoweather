package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_station_weather_param"
)
data class FavoriteStationWeatherParamsEntity(
    @PrimaryKey
    val stationName: String, //主键
    val lon: String = "",
    val lat: String = "",
    val isauto: String = "1",
    val cityids: String = "",
    val cityid: String = "",
    val obtId: String = "",
    val pcity: String = "",
    val parea: String = "",
)


