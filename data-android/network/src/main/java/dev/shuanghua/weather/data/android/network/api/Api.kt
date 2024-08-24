package dev.shuanghua.weather.data.android.network.api

object Api2 {
	const val defaultCityId = "28060159493"
	const val UID = "MV14fND4imK31h2Hsztq"
	const val RAINM = "1"
	const val BASE_URL = "https://szqxapp1.121.com.cn/"

	fun getAqiWebUrl(cityId: String): String {
		return "${BASE_URL}sztq-app/v6/client/h5/aqi?cityid=$cityId"
	}

	fun getImageUrl(iconPathName: String): String {
		return "${BASE_URL}$iconPathName"
	}
}