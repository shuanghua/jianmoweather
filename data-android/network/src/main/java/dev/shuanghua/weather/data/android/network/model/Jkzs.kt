package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Jkzs(
    @field:Json(name = "chenlian") val chenlian: Chenlian?,
    @field:Json(name = "chuanyi") val chuanyi: Chuanyi?,
    @field:Json(name = "co") val co: Co?,
    @field:Json(name = "gaowen") val gaowen: Gaowen?,
    @field:Json(name = "liugan") val liugan: Liugan?,
    @field:Json(name = "luyou") val luyou: Luyou?,
    @field:Json(name = "meibian") val meibian: Meibian?,
    @field:Json(name = "shushidu") val shushidu: Shushidu?,
    @field:Json(name = "ziwaixian") val ziwaixian: Ziwaixian?,
)