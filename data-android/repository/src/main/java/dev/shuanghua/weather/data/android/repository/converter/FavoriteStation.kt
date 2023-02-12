package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.SzwModel

fun SzwModel.asFavoriteStation() = FavoriteStation(
    cityId = cityid,
    stationName = stationName,
    temperature = t,
    weatherStatus = hourForeList[0].weatherstatus,
    weatherIcon = "${ShenZhenApi.IMAGE_URL}${hourForeList[0].wtype}",
    rangeT = "$minT~$maxT"
)


