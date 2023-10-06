package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Element(
	@field: Json(name = "r24h") val r24h: String,
	@field: Json(name = "r01h") val r01h: String,
	@field: Json(name = "pa") val pa: String,
	@field: Json(name = "ws") val ws: String,
	@field: Json(name = "wd") val wd: String,
	@field: Json(name = "rh") val rh: String,
)
