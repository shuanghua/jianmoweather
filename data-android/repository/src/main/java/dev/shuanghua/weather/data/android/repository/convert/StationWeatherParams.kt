package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.model.InnerParams

fun FavoriteStationWeatherParams.toInnerParams() = InnerParams(
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    pcity = pcity,
    parea = parea
)

fun FavoriteStationWeatherParamsEntity.asRequestModel(): FavoriteStationWeatherParams =
    FavoriteStationWeatherParams(
        lon = lon,
        lat = lat,
        isauto = isauto,
        cityids = cityids,
        cityid = cityid,
        obtId = obtId,
        pcity = pcity,
        parea = parea
    )

fun InnerParams.toStationParamsEntity(stationName: String) = FavoriteStationWeatherParamsEntity(
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