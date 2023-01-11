package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lunar(
    val info1: String = "",
    val info2: String = "",
    val info3: String = "",
    val info4: String = "",
    val info5: String = "",
)