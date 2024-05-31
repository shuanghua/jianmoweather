package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FavoriteCityWeatherReturn(@SerialName("list") val list: List<ShenZhenFavoriteCityWeather>)


@Serializable
data class ShenZhenFavoriteCityWeather(
	@SerialName("cityName") val cityName: String,
	@SerialName("cityId") val cityId: String,
	@SerialName("t") val currentT: String,
	@SerialName("wtypeCn") val wtypeCn: String, // 多云
	@SerialName("wtypeIcon") val icon: String, // /webcache/appimagesnew/weatherIcon/09.png
	@SerialName("cityWeatherSmallbg") val bgImage: String // /webcache/appimagesnew/bgSmall/city_smallBg2_10.png
)