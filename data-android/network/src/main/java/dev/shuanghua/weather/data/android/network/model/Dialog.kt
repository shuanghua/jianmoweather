package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dialog(
    val messageList: List<Message> = emptyList(),
    val robotIcon4And: String = "",
    val robotIcon4Ios: String = "",
)