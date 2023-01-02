package dev.shuanghua.weather.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteCityWeather(
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