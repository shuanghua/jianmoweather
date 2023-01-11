package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams

fun FavoriteStationWeatherParams.asEntity(stationName: String) = FavoriteStationWeatherParamsEntity(
    stationName = stationName,
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    pcity = pcity,
    parea = parea
)