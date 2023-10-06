package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainWeatherRequest(
	val lon: String = "113.81486",
	val lat: String = "22.756533",
	val pcity: String = "深圳市",
	val parea: String = "宝安区",
	val uid:String = "d6OIg9m36iZ4kri8sztq",
	val rainm:String = "1",

	val cityid: String = "",
	val obtid: String = "",
)
//http://szqxapp1.121.com.cn/sztq-app/v6/client/index?cityid=28060159493&lat=22.756533&lon=113.81486&pcity=%E6%B7%B1%E5%9C%B3%E5%B8%82&parea=%E5%AE%9D%E5%AE%89%E5%8C%BA&rainm=1&uid=d6OIg9m36iZ4kri8sztq