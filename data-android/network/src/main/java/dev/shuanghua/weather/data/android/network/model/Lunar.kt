package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lunar(
	@SerialName("info1") val info1: String = "",
	@SerialName("info2") val info2: String = "",
	@SerialName("info3") val info3: String = "",
	@SerialName("info4") val info4: String = "",
	@SerialName("info5") val info5: String = "",
)