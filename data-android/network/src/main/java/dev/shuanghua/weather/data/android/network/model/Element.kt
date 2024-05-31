package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Element(
	@SerialName("r24h") val r24h: String,
	@SerialName("r01h") val r01h: String,
	@SerialName("pa") val pa: String,
	@SerialName("ws") val ws: String,
	@SerialName("wd") val wd: String,
	@SerialName("rh") val rh: String,
)
