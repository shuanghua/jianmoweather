package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherEntity
import dev.shuanghua.weather.data.android.model.FavoriteStationWeather
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel

fun FavoriteStationWeatherEntity.asExternalModel() = FavoriteStationWeather(
	cityId = cityId,
	stationName = stationName,
	temperature = temperature,
	weatherStatus = weatherStatus,
	weatherIcon = weatherIcon,
	rangeT = rangeT
)


fun FavoriteStationWeather.asEntity() = FavoriteStationWeatherEntity(
	cityId = cityId,
	stationName = stationName,
	temperature = temperature,
	weatherStatus = weatherStatus,
	weatherIcon = weatherIcon,
	rangeT = rangeT
)

fun MainWeatherModel.asFavoriteStationWeather() = FavoriteStationWeather(
	cityId = cityId,
	stationName = stationName,
	temperature = t,
	weatherStatus = hourList[0].weatherstatus,
	weatherIcon = Api2.getImageUrl(hourList[0].weatherpic),
	rangeT = ""
)