package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lunar(
    @field:Json(name = "info1") val info1: String = "",
    @field:Json(name = "info2") val info2: String = "",
    @field:Json(name = "info3") val info3: String = "",
    @field:Json(name = "info4") val info4: String = "",
    @field:Json(name = "info5") val info5: String = "",
)