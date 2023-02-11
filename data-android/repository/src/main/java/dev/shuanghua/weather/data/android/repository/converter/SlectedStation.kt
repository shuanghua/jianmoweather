package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.SelectedStationEntity
import dev.shuanghua.weather.data.android.model.SelectedStation

fun SelectedStation.asWeatherEntity() = SelectedStationEntity(
    obtId = obtId,
    isLocation = isLocation
)

fun SelectedStationEntity.asExternalModel() = SelectedStation(
    obtId = obtId,
    isLocation = isLocation
)