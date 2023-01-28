package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.network.api.ShenZhenWeatherApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather

fun ShenZhenWeather.asFavoriteStation() = FavoriteStation(
    cityId = cityid,
    stationName = stationName,
    temperature = t,
    weatherStatus = hourForeList[0].weatherstatus,
    weatherIcon = "${ShenZhenWeatherApi.IMAGE_URL}${hourForeList[0].wtype}",
    rangeT = "$minT~$maxT"
)


