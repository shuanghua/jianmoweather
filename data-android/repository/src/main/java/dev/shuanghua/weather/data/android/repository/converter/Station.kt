package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.model.Station

fun Station.asWeatherEntity() = StationEntity(
    districtName = districtName,
    stationId = stationId,
    stationName = stationName
)

fun StationEntity.asExternalModel() = Station(
    districtName = districtName,
    stationId = stationId,
    stationName = stationName
)