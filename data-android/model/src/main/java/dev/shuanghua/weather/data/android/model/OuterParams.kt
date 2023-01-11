package dev.shuanghua.weather.data.android.model

data class OuterParams(
    val type: String = Config.type,
    val ver: String = Config.ver,
    val rever: String = Config.rever,
    val net: String = Config.net,

    val pcity: String = "深圳市",
    val parea: String = "",
    val lon: String = "",
    val lat: String = "",

    val gif: String = Config.gif,
    val uid: String = Config.uid,
    val uname: String = Config.uname,
    val token: String = Config.token,
    val os: String = Config.os
)
