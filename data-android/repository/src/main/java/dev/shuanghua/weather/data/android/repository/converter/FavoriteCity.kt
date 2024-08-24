package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityWeatherEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
/**
 * entity model to external model
 */
fun FavoriteCityEntity.asExternalModel() = FavoriteCity(
	cityId = cityId,
	cityName = cityName,
	provinceName = provinceName
)


fun FavoriteCity.asEntity() = FavoriteCityEntity(
	cityId = cityId,
	cityName = cityName,
	provinceName = provinceName
)