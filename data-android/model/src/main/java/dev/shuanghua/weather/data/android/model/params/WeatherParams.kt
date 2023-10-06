package dev.shuanghua.weather.data.android.model.params

data class WeatherParams(
	val isAuto: String = "",
	val cityIds: String = "",
	val cityId: String = "",
	val obtId: String = "",
	val cityName: String = "",
	val district: String = "",
	val longitude: String = "",
	val latitude: String = ""
) {
	override fun toString(): String {
		return "WeatherParams:" +
				"{" +
				"cityId:$cityId,} " +
				"obtId:$obtId, " +
				"cityName:$cityName, " +
				"district:$district, " +
				"lon:$longitude, " +
				"lat:$latitude " +
				"}"
	}
}

fun WeatherParams.toMapParams(): MutableMap<String, String> = mutableMapOf(
	"cityid" to cityId,
	"pcity" to cityName,
	"parea" to district,
	"lon" to longitude,
	"lat" to latitude,
	"obtid" to obtId,
	"uid" to "d6OIg9m36iZ4kri8sztq",
	"rainm" to "1"
)