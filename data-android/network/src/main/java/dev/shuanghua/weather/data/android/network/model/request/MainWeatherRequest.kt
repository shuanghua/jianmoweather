package dev.shuanghua.weather.data.android.network.model.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainWeatherRequest(
	val cityid: String = "",
	val obtid: String = "",
	val lon: String = "",
	val lat: String = "",
	val pcity: String = "",
	val parea: String = "",
	val uid: String = "d6OIg9m36iZ4kri8sztq",
	val rainm: String = "1",
)
