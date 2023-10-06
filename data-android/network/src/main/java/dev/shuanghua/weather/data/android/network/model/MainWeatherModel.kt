package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 深圳天气网络模型
 */
@JsonClass(generateAdapter = true)
data class MainWeatherModel(
	@field:Json(name = "cityid") val cityId: String = "",
	@field:Json(name = "cityName") val cityName: String = "",
	@field:Json(name = "stationName") val stationName: String = "",
	@field:Json(name = "t") val t: String = "",
	@field:Json(name = "currTime") val currTime: String = "",
	@field:Json(name = "date") val date: String = "",
	@field:Json(name = "desc") val desc: String = "",
	@field:Json(name = "obtid") val obtId: String = "",

	@field:Json(name = "iconObj") val aqiObj: AqiWrapper?,
	@field:Json(name = "element") val element: Element?,
	@field:Json(name = "indexOfLiving") val livingIndex: LivingIndex?,// 仅支持深圳市
	@field:Json(name = "halfCircle") val sunTime: SunTime?,
	@field:Json(name = "lunar") val lunar: Lunar?,

	@field:Json(name = "dayList") val dayList: List<DayWeather> = emptyList(),
	@field:Json(name = "hourForeList") val hourList: List<HourWeather> = emptyList(),
	@field:Json(name = "alarmList") val alarmList: List<Alarm> = emptyList(),
)