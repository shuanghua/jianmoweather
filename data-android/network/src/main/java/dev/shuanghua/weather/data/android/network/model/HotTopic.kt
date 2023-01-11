package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HotTopic(
    val color: String = "",
    val hpImbtime: String = "",
    val hpImetime: String = "",
    val issele: String = "",
    val wnid: String = "",
    val wnname: String = "",
    val wnurl: String = "",
)