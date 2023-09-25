package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HourFore(
    @field:Json(name = "hour") val hour: String = "",
    @field:Json(name = "rain") val rain: String = "",
    @field:Json(name = "t") val t: String = "",
    @field:Json(name = "wd") val wd: String = "",
    @field:Json(name = "wf") val wf: String = "",
    @field:Json(name = "wtype") val weatherpic: String = "",
    @field:Json(name = "weatherstatus") val weatherstatus: String = "",
)