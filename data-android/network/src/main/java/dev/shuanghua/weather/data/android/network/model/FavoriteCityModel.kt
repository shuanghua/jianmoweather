package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FavoriteCityWeatherReturn(@Json(name = "list") val list: List<ShenZhenFavoriteCityWeather>)


@JsonClass(generateAdapter = true)
data class ShenZhenFavoriteCityWeather(
	@field:Json(name = "cityName") val cityName: String,
	@field:Json(name = "cityId") val cityId: String,
	@field:Json(name = "t") val currentT: String,
	@field:Json(name = "wtypeCn") val wtypeCn: String, // 多云
	@field:Json(name = "wtypeIcon") val icon: String, // /webcache/appimagesnew/weatherIcon/09.png
	@field:Json(name = "cityWeatherSmallbg") val bgImage: String // /webcache/appimagesnew/bgSmall/city_smallBg2_10.png

)