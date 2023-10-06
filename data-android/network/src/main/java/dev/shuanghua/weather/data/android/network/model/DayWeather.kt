package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DayWeather(
	@field:Json(name = "date") val date: String?,
	@field:Json(name = "week") val week: String?,
	@field:Json(name = "desc") val desc: String?,
	@field:Json(name = "maxT") val maxT: String?,
	@field:Json(name = "minT") val minT: String?,
	@field:Json(name = "wd") val wd: String?,  // 风向
	@field:Json(name = "wf") val wf: String?,  // 风力
	@field:Json(name = "wtype") val wtype: String? // 图片
)