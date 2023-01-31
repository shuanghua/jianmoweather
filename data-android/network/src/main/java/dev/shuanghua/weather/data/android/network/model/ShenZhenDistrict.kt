package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DistrictReturn(
    @field:Json(name = "list") val list: List<ShenZhenDistrict>?
)


@JsonClass(generateAdapter = true)
data class ShenZhenDistrict(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "list") val list: MutableList<ShenZhenStation>
)