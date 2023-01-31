package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HalfCircleIwatch(
    @field:Json(name = "downicon") val downicon: String = "",
    @field:Json(name = "halfCircleList") val halfCircleList: List<HalfCircleXX> = emptyList(),
    @field:Json(name = "size") val size: Int = 0,
    @field:Json(name = "sundown") val sundown: String = "",
    @field:Json(name = "sunup") val sunup: String = "",
    @field:Json(name = "upicon") val upicon: String = "",
)