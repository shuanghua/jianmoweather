package dev.shuanghua.weather.data.android.network.api

object Api2 {
	private const val HOST = "https://szqxapp1.121.com.cn/"
	const val BASE_URL = HOST


	fun getAqiWebUrl(cityId: String): String {
		return "${HOST}sztq-app/v6/client/h5/aqi?cityid=$cityId"
	}


	fun getImageUrl(iconPathName: String): String {
		return "${HOST}${iconPathName}"
	}
}