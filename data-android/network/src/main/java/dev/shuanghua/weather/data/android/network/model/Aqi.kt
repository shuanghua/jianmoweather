package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.Serializable


@Serializable
data class AqiWrapper(val aqi: Aqi? = null)

@Serializable
data class Aqi(
	val aqi: String = "", // 24
	val aqic: String = "", // 优
	val icon: String = "", // 图标地址
	val url: String = "",
)