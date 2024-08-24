package dev.shuanghua.weather.data.android.model.params

data class DistrictParams(
	val cityId: String = "",
	val uid: String = "",
	val longitude: String = "",
	val latitude: String = ""
)

fun DistrictParams.asRequestMap(): MutableMap<String, String> = mutableMapOf(
	"cityid" to cityId,
	"lon" to longitude,
	"lat" to latitude,
	"uid" to "d6OIg9m36iZ4kri8sztq"
)