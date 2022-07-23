package dev.shuanghua.weather.data.network

import dev.shuanghua.weather.data.db.entity.OuterParam

//@Entity
//data class OuterParam(
//    val type: String = OuterParam.type,
//    val ver: String = OuterParam.ver,
//    val rever: String = OuterParam.rever,
//    val net: String = OuterParam.net,
//
//    @PrimaryKey
//    val pcity: String = "",
//    val parea: String = "",
//    val lon: String = "",
//    val lat: String = "",
//
//    val gif: String = OuterParam.gif,
//    val uid: String = OuterParam.uid,
//    val uname: String = OuterParam.uname,
//    val token: String = OuterParam.token,
//    val os: String = OuterParam.os,
//) {
//    companion object {
//        val type = "1"
//        val ver = "v5.7.0"
//        val rever = "578"
//        val net = "WIFI"
//        val gif = "true"
//        val uid = "Rjc4qedi323eK4PGsztqsztq"
//        val uname = ""
//        val token = ""
//        val os = "android30"
//
//        val HEIGHT = "2215"
//        val WIDTH = "1080"
//        val ISLOCATION = "0"
//        val CITYIDS = "28060159493,32010145005,28010159287,02010058362,01010054511"
//        val DEFAULT = OuterParam()
//    }
//}


//@Entity
//data class WeatherParam(
//    val lon: String = "", //
//    val lat: String = "", //
//    val isauto: String = OuterParam.ISLOCATION,
//    val cityids: String = OuterParam.CITYIDS,
//    val cityid: String = "", //
//    val w: String = OuterParam.WIDTH,
//    val h: String = OuterParam.HEIGHT,
//    @PrimaryKey
//    val pcity: String = "",
//    val parea: String = "",
//    val gif: String = OuterParam.gif
//) {
//    companion object {
//        // 只有首次安装APP 并且 定位失败 才会使用 APP 设定的默认参数
//        val DEFAULT = WeatherParam()
//    }
//}


data class FavoriteWeatherParam(
    val isauto: String,
    val cityids: String = OuterParam.CITYIDS //
)


data class FindCitIdyByKeyWordsParam(
    val key: String, //
    val cityids: String
)

data class CityListByProvinceIdParam(
    val provId: String, //
    val cityids: String
)

data class StationListParam(
    val cityid: String = "",
    val obtid: String = "",
    val lon: String = "",
    val lat: String = ""
) {
    companion object {
        val DEFAULT = StationListParam()
    }
}

