package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel

internal fun MainWeatherModel.cleanSunTime(): Pair<String, String> {
	var sunUp = sunTime?.sunup ?: ""
	var sunDown = sunTime?.sundown ?: ""
	if (sunUp[0] != '0') {//10点到晚上11：59
		sunUp = sunDown.also { sunDown = sunUp }
	}
	return Pair(sunUp, sunDown)
}

internal fun MainWeatherModel.cleanCalendar(): String {
	return lunar?.run { "$info1 $info2 $info3 $info4 $info5" } ?: ""
}

internal fun MainWeatherModel.cleanAirQuality(): String {
	return aqiObj?.aqi?.let { it.aqi + "·" + it.aqic } ?: ""
}

internal fun MainWeatherModel.cleanAirQualityIcon(): String {
	return aqiObj?.aqi?.let { Api2.getImageUrl(it.icon) } ?: ""
}
