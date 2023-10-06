package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.network.model.CityModel

fun City.asWeatherEntity() = CityEntity(
	provinceName = provinceName,
	name = name,
	id = id
)


/**
 * 数据库模型 -> Ui 模型
 */
fun CityEntity.asExternalModel() = City(
	provinceName = provinceName,
	name = name,
	id = id
)

/**
 * 网络模型 -> Ui 模型
 */
fun CityModel.asExternalModel(provinceName: String) = City(
	provinceName = provinceName,
	name = cityName,
	id = cityId
)


/**
 * 网络模型 -> 数据库模型
 */
fun CityModel.asEntity(provinceName: String) = CityEntity(
	provinceName = provinceName,
	name = cityName,
	id = cityId
)