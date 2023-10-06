package dev.shuanghua.weather.data.android.model.params

data class FavoriteCityParams(
	val cityIds: String = "",
	val longitude: String = "",
	val latitude: String = "",
	val uid: String = "d6OIg9m36iZ4kri8sztq"
)

fun FavoriteCityParams.toMapParams(): MutableMap<String, String> = mutableMapOf(
	"cityIds" to cityIds,
	"lon" to longitude,
	"lat" to latitude,
	"uid" to uid,
)