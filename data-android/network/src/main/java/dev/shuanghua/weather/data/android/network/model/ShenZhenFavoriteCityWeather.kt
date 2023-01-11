package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteCityWeatherReturn(@Json(name = "list") val list: List<ShenZhenFavoriteCityWeather>)

@JsonClass(generateAdapter = true)
data class ShenZhenFavoriteCityWeather(
    val cityName: String,
    val cityType: String,
    val cityid: String,
    val isauto: String,
    val maxT: String,
    val minT: String,
    val wnow: String,
    val wnownew: String,
    val wtype: String,
)