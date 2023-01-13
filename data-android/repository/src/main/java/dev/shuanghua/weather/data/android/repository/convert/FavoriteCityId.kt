package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.model.FavoriteCityId

fun FavoriteCityIdEntity.asExternalModel() = FavoriteCityId(
    id = id
)

fun FavoriteCityId.asEntity() = FavoriteCityIdEntity(
    id = id
)