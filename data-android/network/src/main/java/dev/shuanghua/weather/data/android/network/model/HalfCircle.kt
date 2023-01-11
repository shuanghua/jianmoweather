package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HalfCircle(
    val downicon: String,
    val halfCircleList: List<HalfCircleX> = emptyList(),
    val size: Int = 0,
    val sundown: String = "",
    val sunup: String = "",
    val upicon: String = "",
)