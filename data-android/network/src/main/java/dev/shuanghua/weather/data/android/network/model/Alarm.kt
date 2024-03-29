package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Alarm(
    @field:Json(name = "icon") val icon: String = "",
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "url") val url: String = "",
)