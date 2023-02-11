package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.WeatherParamsEntity
import dev.shuanghua.weather.data.android.model.params.WeatherParams

fun WeatherParamsEntity.asExternalModel(
) = WeatherParams(
    lon = lon,
    lat = lat,
    isAuto = isAuto,
    cityIds = cityIds,
    cityId = cityId,
    obtId = obtId,
    cityName = cityName,
    district = district
)

fun WeatherParams.asEntity(
    stationName: String
) = WeatherParamsEntity(
    stationName = stationName,
    lon = lon,
    lat = lat,
    isAuto = isAuto,
    cityIds = cityIds,
    cityId = cityId,
    obtId = obtId,
    cityName = cityName,
    district = district
)