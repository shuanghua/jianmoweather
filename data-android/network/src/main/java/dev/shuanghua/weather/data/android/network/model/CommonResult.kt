package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonResult<out T>(@field:Json(name = "result") val data: T)




