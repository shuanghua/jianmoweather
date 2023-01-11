package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Aqi(
    val aqi: String = "", // 24
    val aqic: String = "", // 优
    val icon: String = "",
)