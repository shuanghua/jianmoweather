package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.android.model.FavoriteCity

fun FavoriteCity.asEntity() = FavoriteCityEntity(
    cityId = cityId,
    cityName = cityName
)

fun FavoriteCityEntity.asExternalModel() = FavoriteCity(
    cityId = cityId,
    cityName = cityName
)