package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.SelectedStationEntity
import dev.shuanghua.weather.data.android.model.SelectedStation

fun SelectedStation.asWeatherEntity() = SelectedStationEntity(
	isLocation = isLocation,
	stationId = stationId,
	districtName = districtName,
	stationName = stationName,
)

fun SelectedStationEntity.asExternalModel() = SelectedStation(
	isLocation = isLocation,
	stationId = stationId,
	districtName = districtName,
	stationName = stationName,
)