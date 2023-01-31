package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HotTopic(
    @field:Json(name = "color") val color: String = "",
    @field:Json(name = "hpImbtime") val hpImbtime: String = "",
    @field:Json(name = "hpImetime") val hpImetime: String = "",
    @field:Json(name = "issele") val issele: String = "",
    @field:Json(name = "wnid") val wnid: String = "",
    @field:Json(name = "wnname") val wnname: String = "",
    @field:Json(name = "wnurl") val wnurl: String = "",
)