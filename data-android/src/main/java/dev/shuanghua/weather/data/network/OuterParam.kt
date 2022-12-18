package dev.shuanghua.weather.data.network

data class OuterParam(
    val type: String = CommonParam.type,
    val ver: String = CommonParam.ver,
    val rever: String = CommonParam.rever,
    val net: String = CommonParam.net,

//    @PrimaryKey
    val pcity: String = "深圳市",
    val parea: String = "",
    val lon: String = "",
    val lat: String = "",

    val gif: String = CommonParam.gif,
    val uid: String = CommonParam.uid,
    val uname: String = CommonParam.uname,
    val token: String = CommonParam.token,
    val os: String = CommonParam.os
)
