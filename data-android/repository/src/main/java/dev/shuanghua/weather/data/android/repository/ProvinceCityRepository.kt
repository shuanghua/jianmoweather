package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.CityDao
import dev.shuanghua.weather.data.android.database.dao.ProvinceDao
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.model.params.ProvinceCityParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ProvinceCityModel
import dev.shuanghua.weather.data.android.network.model.ProvinceModel
import dev.shuanghua.weather.data.android.repository.converter.asEntity
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProvinceCityRepository(
	private val provinceDao: ProvinceDao,
	private val cityDao: CityDao,
	private val network: NetworkDataSource
) {
	suspend fun updateProvinceCityList() {
		val networkModel: ProvinceCityModel = network.getProvinceCityList(ProvinceCityParams())
		val provinceEntityList = networkModel.provinceList.map { it.asEntity() }
		val cityEntityList = networkModel.provinceList.flatMap { provinceModel: ProvinceModel ->
			provinceModel.cityList.map { it.asEntity(provinceModel.provName) }
		}

		provinceDao.insertProvince(provinceEntityList)
		cityDao.insertCityList(cityEntityList)
	}


	fun observerProvinces(): Flow<List<Province>> {
		return provinceDao.observerProvinces().map { provinceEntityList ->
			provinceEntityList.map { it.asExternalModel() }
		}
	}


	fun observerCityList(provinceName: String): Flow<List<City>> {
		return cityDao.observerCityList(provinceName)
			.map { it.map(CityEntity::asExternalModel) }
	}
}

