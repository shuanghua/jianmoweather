package dev.shuanghua.weather.data.android.model

data class MainWeatherParams(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = Config.isLocation,
    val cityids: String = Config.cityids,
    val cityid: String = "",
    val obtId: String = "",
    val w: String = Config.width,
    val h: String = Config.height,
    val pcity: String = "",
    val parea: String = "",
    val gif: String = Config.gif,
)

fun MainWeatherParams.asInnerParams():InnerParams = InnerParams(
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    w = w,
    h = h,
    pcity = pcity,
    parea = parea,
    gif = gif
)

fun MainWeatherParams.asOuterParams() = OuterParams(
    pcity = pcity,
    parea = parea,
    lon = lon,
    lat = lat,
)