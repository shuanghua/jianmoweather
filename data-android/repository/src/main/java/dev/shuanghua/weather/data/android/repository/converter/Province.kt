package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.ProvinceEntity
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.network.model.ProvinceModel


fun ProvinceEntity.asExternalModel() = Province(
	name = name
)


fun ProvinceModel.asEntity() = ProvinceEntity(
	name = provName
)