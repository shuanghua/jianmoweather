package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityId(
    val cityid: String = "",
    val isauto: String = "",
)