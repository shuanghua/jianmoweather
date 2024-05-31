package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayWeather(
	@SerialName("fdatetime") val date: String?,
	@SerialName("week") val week: String?,
	@SerialName("desc") val desc: String?,
	@SerialName("maxT") val maxT: String?,
	@SerialName("minT") val minT: String?,
	@SerialName("wd") val wd: String?,  // 风向
	@SerialName("wf") val wf: String?,  // 风力
	@SerialName("wtype") val wtype: String? // 图片
)