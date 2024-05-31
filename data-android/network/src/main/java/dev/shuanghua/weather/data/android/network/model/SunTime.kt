package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SunTime(
     @SerialName("downicon") val downicon: String,
     @SerialName("sundown") val sundown: String = "",
     @SerialName("sunup") val sunup: String = "",
     @SerialName("upicon") val upicon: String = "",
)