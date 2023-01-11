package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourFore(
    val desc_en: String = "",
    val hour: String = "",
    val hour_en: String = "",
    val rain: String = "",
    val t: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val weatherstatus: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)