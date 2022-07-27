package dev.shuanghua.weather.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShenZhenDistrict(
    @Json(name = "name")val name: String,
    @Json(name = "list")val list: MutableList<ShenZhenStation>
)

@JsonClass(generateAdapter = true)
data class ShenZhenStation(
    @Json(name = "obtid") val stationId: String,
    @Json(name = "issele") val isSelect: String,
    @Json(name = "obtname") val stationName: String
)