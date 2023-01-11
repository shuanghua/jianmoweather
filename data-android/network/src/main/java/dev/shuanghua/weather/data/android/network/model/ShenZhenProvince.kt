package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProvinceReturn(@Json(name = "list") val list: List<ShenZhenProvince>)

@JsonClass(generateAdapter = true)
data class ShenZhenProvince(
    @Json(name = "provName") val name: String,
    @Json(name = "provId") val id: String
)