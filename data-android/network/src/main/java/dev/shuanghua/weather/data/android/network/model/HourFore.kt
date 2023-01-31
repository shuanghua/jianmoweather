package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HourFore(
    @field:Json(name = "desc_en") val desc_en: String = "",
    @field:Json(name = "hour") val hour: String = "",
    @field:Json(name = "hour_en") val hour_en: String = "",
    @field:Json(name = "rain") val rain: String = "",
    @field:Json(name = "t") val t: String = "",
    @field:Json(name = "wd") val wd: String = "",
    @field:Json(name = "wd_en") val wd_en: String = "",
    @field:Json(name = "weatherpic") val weatherpic: String = "",
    @field:Json(name = "weatherstatus") val weatherstatus: String = "",
    @field:Json(name = "wf") val wf: String = "",
    @field:Json(name = "wf_en") val wf_en: String = "",
    @field:Json(name = "wtype") val wtype: String = "",
)