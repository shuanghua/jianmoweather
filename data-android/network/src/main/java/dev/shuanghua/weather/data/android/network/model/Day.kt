package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Day(
    @field:Json(name = "date") val date: String = "",
    @field:Json(name = "desc") val desc: String = "",
    @field:Json(name = "desc1") val desc1: String = "",
    @field:Json(name = "desc_en") val desc_en: String = "",
    @field:Json(name = "maxT") val maxT: String = "",
    @field:Json(name = "minT") val minT: String = "",
    @field:Json(name = "wd") val wd: String = "",
    @field:Json(name = "wd_en") val wd_en: String = "",
    @field:Json(name = "weatherpic") val weatherpic: String = "",
    @field:Json(name = "week") val week: String = "",
    @field:Json(name = "week_en") val week_en: String = "",
    @field:Json(name = "wf") val wf: String = "",
    @field:Json(name = "wf_en") val wf_en: String = "",
    @field:Json(name = "wtype") val wtype: String = "",
)