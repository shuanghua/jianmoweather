package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.model.Station

fun Station.asEntity() = StationEntity(
    districtName = districtName,
    stationId = stationId,
    stationName = stationName
)

fun StationEntity.asExternalModel() = Station(
    districtName = districtName,
    stationId = stationId,
    stationName = stationName
)