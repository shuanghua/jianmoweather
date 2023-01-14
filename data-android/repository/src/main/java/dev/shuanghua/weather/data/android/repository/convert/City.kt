package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.network.model.ShenZhenCity

fun City.asEntity() = CityEntity(
    provinceName = provinceName,
    name = name,
    id = id
)

fun CityEntity.asExternalModel() = City(
    provinceName = provinceName,
    name = name,
    id = id
)

fun ShenZhenCity.asEntity(provinceName: String) = CityEntity(
    provinceName = provinceName,
    name = name,
    id = id
)

fun ShenZhenCity.asExternalModel(provinceName: String) = City(
    provinceName = provinceName,
    name = name,
    id = id
)