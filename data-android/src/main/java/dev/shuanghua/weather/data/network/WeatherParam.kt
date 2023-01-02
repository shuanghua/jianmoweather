package dev.shuanghua.weather.data.network

import dev.shuanghua.weather.data.db.entity.FavoriteStationParamEntity

data class InnerParam(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = CommonParam.isLocation,
    val cityids: String = CommonParam.cityids,
    val cityid: String = "",
    val obtId: String = "",
    val w: String = CommonParam.width,
    val h: String = CommonParam.height,
    val pcity: String = "",
    val parea: String = "",
    val gif: String = CommonParam.gif,
)

fun InnerParam.asOuterParam() = OuterParam(
    pcity = pcity,
    parea = parea,
    lon = lon,
    lat = lat,
)

fun InnerParam.toStationParamEntity(stationName: String) = FavoriteStationParamEntity(
    stationName = stationName,
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    parea = parea,
    pcity = pcity
)


data class MainWeatherParam(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = CommonParam.isLocation,
    val cityids: String = CommonParam.cityids,
    val cityid: String = "",
    val obtId: String = "",
    val w: String = CommonParam.width,
    val h: String = CommonParam.height,
    val pcity: String = "",
    val parea: String = "",
    val gif: String = CommonParam.gif,
)

data class FavoriteCityParam(
    val isauto: String,
    val cityids: String,
    val lon: String,
    val lat: String,
)

/**
 * 主页的天气请求参数（站点切换也使用该参数）
 */
fun InnerParam.asMainWeatherParam() = MainWeatherParam(
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

fun InnerParam.asFavoriteWeatherParam() = FavoriteCityParam(
    isauto = isauto,
    cityids = cityids,
    lon = lon,
    lat = lat,
)

