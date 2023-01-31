package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityId(
    @field:Json(name = "cityid") val cityid: String = "",
    @field:Json(name = "isauto") val isauto: String = "",
)