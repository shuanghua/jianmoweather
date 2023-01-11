package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.SelectedStationEntity
import dev.shuanghua.weather.data.android.model.SelectedStation

fun SelectedStation.asEntity() = SelectedStationEntity(
    obtId = obtId,
    isLocation = isLocation
)

fun SelectedStationEntity.asExternalModel() = SelectedStation(
    obtId = obtId,
    isLocation = isLocation
)