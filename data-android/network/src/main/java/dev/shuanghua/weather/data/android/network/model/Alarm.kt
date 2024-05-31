package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Alarm(
    val icon: String = "",
    val name: String = "",
    val url: String = "",
)