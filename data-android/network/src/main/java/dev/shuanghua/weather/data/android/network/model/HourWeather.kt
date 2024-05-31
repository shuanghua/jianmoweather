package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class HourWeather(
	@SerialName("hour") val hour: String = "",
	@SerialName("rain") val rain: String = "",
	@SerialName("t") val t: String = "",
	@SerialName("wd") val wd: String = "",
	@SerialName("wf") val wf: String = "",
	@SerialName("wtype") val weatherpic: String = "",
	@SerialName("weatherstatus") val weatherstatus: String = "",
)