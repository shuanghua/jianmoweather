package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainWeatherRequest(
	val lon: String = "113.81032715062695",
	val lat: String = "22.760415390391916",
	val pcity: String = "深圳市",
	val parea: String = "宝安区",
	val uid:String = "d6OIg9m36iZ4kri8sztq",
	val rainm:String = "1",

	val cityid: String = "",
	val obtid: String = "",
)