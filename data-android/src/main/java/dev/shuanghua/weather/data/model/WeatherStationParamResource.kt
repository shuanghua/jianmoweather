package dev.shuanghua.weather.data.model

import dev.shuanghua.weather.data.network.InnerParam

data class WeatherStationParamResource(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = "1",
    val cityids: String = "",
    val cityid: String = "",
    val obtId: String = "",
    val pcity: String = "",
    val parea: String = "",
)

fun WeatherStationParamResource.toInnerParam() = InnerParam(
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    pcity = pcity,
    parea = parea
)
