package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.network.api.ShenZhenRetrofitApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather

fun ShenZhenWeather.asFavoriteStation() = FavoriteStation(
    stationName = stationName,
    temperature = t,
    weatherStatus = hourForeList[0].weatherstatus,
    weatherIcon = "${ShenZhenRetrofitApi.ICON_HOST}${hourForeList[0].wtype}",
    rangeT = "$minT~$maxT"
)


