package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShenZhenStation(
    @Json(name = "obtid") val stationId: String,
    @Json(name = "issele") val isSelect: String,
    @Json(name = "obtname") val stationName: String
)