package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.FavoriteStationParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteStationParams

fun FavoriteStationParamsEntity.asExternalModel(
) = FavoriteStationParams(
	isAutoLocation = isAutoLocation,
	cityId = cityId,
	stationId = stationId,
	stationName = stationName,
	latitude = lat,
	longitude = lon,
	cityName = pcity,
	district = parea,
)

fun FavoriteStationParams.asEntity(
) = FavoriteStationParamsEntity(
	isAutoLocation = isAutoLocation,
	cityId = cityId,
	stationId = stationId,
	stationName = stationName,
	lat = latitude,
	lon = longitude,
	pcity = cityName,
	parea = district,
)

