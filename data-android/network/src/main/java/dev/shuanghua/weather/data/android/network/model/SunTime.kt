package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SunTime(
     @field:Json(name = "downicon") val downicon: String,
     @field:Json(name = "sundown") val sundown: String = "",
     @field:Json(name = "sunup") val sunup: String = "",
     @field:Json(name = "upicon") val upicon: String = "",
)