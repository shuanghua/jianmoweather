package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.network.model.ShenZhenCity

fun City.asWeatherEntity() = CityEntity(
    provinceName = provinceName,
    name = name,
    id = id
)

fun CityEntity.asExternalModel() = City(
    provinceName = provinceName,
    name = name,
    id = id
)

fun ShenZhenCity.asWeatherEntity(provinceName: String) = CityEntity(
    provinceName = provinceName,
    name = name,
    id = id
)

fun ShenZhenCity.asExternalModel(provinceName: String) = City(
    provinceName = provinceName,
    name = name,
    id = id
)
