package dev.shuanghua.weather.data.android.model.params

data class WeatherParams(
	val isAuto: String = "",
	val cityIds: String = "",
	val cityId: String = "",
	val obtId: String = "",
	val cityName: String = "",
	val district: String = "",
	val lon: String = "",
	val lat: String = ""
) {
	override fun toString(): String {
		return "WeatherParams:" +
				"{" +
				"cityId:$cityId,} " +
				"obtId:$obtId, " +
				"cityName:$cityName, " +
				"district:$district, " +
				"lon:$lon, " +
				"lat:$lat " +
				"}"
	}
}