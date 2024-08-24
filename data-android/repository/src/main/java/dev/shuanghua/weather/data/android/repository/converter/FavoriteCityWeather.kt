package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.FavoriteCityWeatherEntity
import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

/**
 * network model to external model
 */
fun ShenZhenFavoriteCityWeather.asExternalModel() = FavoriteCityWeather(
	cityName = cityName,
	cityId = cityId,
	currentT = currentT,
	bgImageNew = Api2.getImageUrl(bgImage),
	iconUrl = Api2.getImageUrl(icon)
)

/**
 * entity model to external model
 */
fun FavoriteCityWeatherEntity.asExternalModel() = FavoriteCityWeather(
	cityId = cityId,
	cityName = cityName,
	provinceName = provinceName,
	isAutoLocation = isAutoLocation,
	currentT = currentT,
	bgImageNew = bgImageNew,
	iconUrl = iconUrl,
)

/**
 * external model to entity model
 */
fun FavoriteCityWeather.asEntity() = FavoriteCityWeatherEntity(
	cityId = cityId,
	cityName = cityName,
	provinceName = provinceName,
	isAutoLocation = isAutoLocation,
	currentT = currentT,
	bgImageNew = bgImageNew,
	iconUrl = iconUrl,
)