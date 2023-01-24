package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Jkzs(
    val chenlian: Chenlian?,
    val chuanyi: Chuanyi?,
    val co: Co?,
    val gaowen: Gaowen?,
    val liugan: Liugan?,
    val luyou: Luyou?,
    val meibian: Meibian?,
    val shushidu: Shushidu?,
    val ziwaixian: Ziwaixian?,
)