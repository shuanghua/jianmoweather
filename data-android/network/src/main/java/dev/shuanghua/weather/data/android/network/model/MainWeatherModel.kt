package dev.shuanghua.weather.data.android.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 深圳天气网络模型
 */
@Serializable
data class MainWeatherModel(
	@SerialName("cityid") val cityId: String = "",
	@SerialName("cityName") val cityName: String = "",
	@SerialName("stationName") val stationName: String = "",
	@SerialName("t") val t: String = "",
	@SerialName("currTime") val currTime: String = "",
	@SerialName("date") val date: String = "",
	@SerialName("desc") val desc: String = "",
	@SerialName("obtid") val obtId: String = "",

	@SerialName("iconObj") val aqiObj: AqiWrapper?,
	@SerialName("element") val element: Element?,
	@SerialName("indexOfLiving") val livingIndex: LivingIndex?,// 仅支持深圳市
	@SerialName("halfCircle") val sunTime: SunTime?,
	@SerialName("lunar") val lunar: Lunar?,

	@SerialName("dayList") val dayList: List<DayWeather> = emptyList(),
	@SerialName("hourForeList") val             hourList: List<HourWeather> = emptyList(),
	@SerialName("alarmList") val alarmList: List<Alarm> = emptyList(),
)