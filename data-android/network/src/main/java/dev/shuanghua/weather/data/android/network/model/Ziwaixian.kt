package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ziwaixian(
    @field:Json(name = "level") val level: String = "",
    @field:Json(name = "level_advice") val level_advice: String = "",
    @field:Json(name = "level_desc") val level_desc: String = "",
    @field:Json(name = "title") val title: String = "",
)