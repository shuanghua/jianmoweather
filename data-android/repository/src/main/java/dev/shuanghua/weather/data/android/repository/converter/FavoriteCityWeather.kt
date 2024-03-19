package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

fun ShenZhenFavoriteCityWeather.asExternalModel() = FavoriteCity(
	cityName = cityName,
	cityId = cityId,
	currentT = currentT,
	bgImageNew = Api2.getImageUrl(bgImage),
	iconUrl = Api2.getImageUrl(icon)
)