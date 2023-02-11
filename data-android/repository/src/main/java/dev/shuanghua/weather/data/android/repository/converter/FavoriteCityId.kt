package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.model.FavoriteCityId

fun FavoriteCityIdEntity.asExternalModel() = FavoriteCityId(
    id = id
)

fun FavoriteCityId.asWeatherEntity() = FavoriteCityIdEntity(
    id = id
)