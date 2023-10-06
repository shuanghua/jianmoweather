package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel

fun MainWeatherModel.asFavoriteStation() = FavoriteStation(
	cityId = cityId,
	stationName = stationName,
	temperature = t,
	weatherStatus = hourList[0].weatherstatus,
	weatherIcon = Api2.getImageUrl(hourList[0].weatherpic),
	rangeT = ""
)


