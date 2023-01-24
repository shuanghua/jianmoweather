package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather

fun ShenZhenWeather.asFavoriteStation() = FavoriteStation(
    cityId = cityid,
    stationName = stationName,
    temperature = t,
    weatherStatus = hourForeList[0].weatherstatus,
    weatherIcon = "${ShenZhenApi.Url_Image}${hourForeList[0].wtype}",
    rangeT = "$minT~$maxT"
)


