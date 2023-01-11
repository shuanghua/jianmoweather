package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meibian(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)