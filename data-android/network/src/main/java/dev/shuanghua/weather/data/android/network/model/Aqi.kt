package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class Aqi(
    @field:Json(name = "aqi") val aqi: String = "", // 24
    @field:Json(name = "aqic") val aqic: String = "", // 优
    @field:Json(name = "icon") val icon: String = "",
)