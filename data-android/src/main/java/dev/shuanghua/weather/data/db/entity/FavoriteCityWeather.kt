package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "favorite_city_weather"
)
data class FavoriteCityWeatherEntity(
    @PrimaryKey
    val cityName: String,
    val cityType: String,
    val cityid: String,
    val isauto: String,
    val maxT: String,
    val minT: String,
    val wnow: String?,
    val wnownew: String,
    val wtype: String,
)