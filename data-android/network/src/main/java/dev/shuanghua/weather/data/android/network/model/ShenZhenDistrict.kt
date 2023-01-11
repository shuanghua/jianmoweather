package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DistrictReturn(@Json(name = "list") val list: List<ShenZhenDistrict>)

@JsonClass(generateAdapter = true)
data class ShenZhenDistrict(
    @Json(name = "name") val name: String,
    @Json(name = "list") val list: MutableList<ShenZhenStation>
)