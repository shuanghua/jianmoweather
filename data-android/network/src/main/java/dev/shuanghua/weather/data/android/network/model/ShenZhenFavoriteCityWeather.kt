package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FavoriteCityWeatherReturn(@Json(name = "list") val list: List<ShenZhenFavoriteCityWeather>)


@JsonClass(generateAdapter = true)
data class ShenZhenFavoriteCityWeather(
    @field:Json(name = "cityName") val cityName: String,
    @field:Json(name = "cityType") val cityType: String,
    @field:Json(name = "cityid") val cityid: String,
    @field:Json(name = "isauto") val isauto: String,
    @field:Json(name = "maxT") val maxT: String,
    @field:Json(name = "minT") val minT: String,
    @field:Json(name = "wnow") val wnow: String,
    @field:Json(name = "wnownew") val wnownew: String,
    @field:Json(name = "wtype") val wtype: String,
)