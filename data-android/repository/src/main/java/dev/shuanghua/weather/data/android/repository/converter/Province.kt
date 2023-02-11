package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.ProvinceEntity
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.network.model.ShenZhenProvince

fun ShenZhenProvince.asWeatherEntity() = ProvinceEntity(
    name = name,
    id = id
)

fun ProvinceEntity.asExternalModel() = Province(
    name = name,
    id = id
)