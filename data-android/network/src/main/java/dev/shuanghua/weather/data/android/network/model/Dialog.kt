package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Dialog(
    @field:Json(name = "messageList") val messageList: List<Message> = emptyList(),
    @field:Json(name = "robotIcon4And") val robotIcon4And: String = "",
    @field:Json(name = "robotIcon4Ios") val robotIcon4Ios: String = "",
)