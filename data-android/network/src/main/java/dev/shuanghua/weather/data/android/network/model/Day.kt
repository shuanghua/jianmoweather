package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Day(
    val date: String = "",
    val desc: String = "",
    val desc1: String = "",
    val desc_en: String = "",
    val maxT: String = "",
    val minT: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val week: String = "",
    val week_en: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)