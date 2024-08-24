package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.CityDao
import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.dao.ProvinceDao
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.android.database.entity.ProvinceEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ProvinceCityModel
import dev.shuanghua.weather.data.android.repository.converter.asEntity
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface ProvinceCityRepository {
	suspend fun updateProvinceCityList()
	fun observerProvinces(): Flow<List<Province>>
	fun observerCityList(provinceName: String): Flow<List<City>>
	suspend fun saveFavoriteCity(city: City)
	fun observerAllProvinces(): Flow<List<Province>>
}

class ProvinceCityRepositoryImpl(
	private val provinceDao: ProvinceDao,
	private val cityDao: CityDao,
	private val favoriteDao: FavoriteDao,
	private val network: NetworkDataSource,
	private val dispatchers: AppDispatchers,
) : ProvinceCityRepository {
	override suspend fun updateProvinceCityList() = withContext(dispatchers.io) {
//		val networkModel: ProvinceCityModel =network.getProvinceCityList(ProvinceCityParams())// 网络请求
		val networkModel: ProvinceCityModel = network.getProvinceCityListFromAssets()// 使用本地 json 文件
		val (provinceEntities, cityEntities) = networkModel.provinceList.flatMap { province ->
			province.cityList.map { city -> province.asEntity() to city.asEntity(province.provName) }
		}.unzip()
		val job = coroutineScope { // 失败取消全部
			launch { provinceDao.insertProvince(provinceEntities) }
			launch { cityDao.insertCityList(cityEntities) }
		}
		job.join()
	}

	override fun observerAllProvinces(): Flow<List<Province>> {
		val provinceFlow = provinceDao.observerProvinces()
		return provinceFlow
			.onStart { if (provinceFlow.first().isEmpty()) updateProvinceCityList() }
			.map { it.map(ProvinceEntity::asExternalModel) }
			.flowOn(dispatchers.io)
	}

	override fun observerProvinces(): Flow<List<Province>> = flow {
		val provinceEntities = provinceDao.observerProvinces().first()
		if (provinceEntities.isEmpty()) updateProvinceCityList()
		emitAll(provinceDao.observerProvinces().map { it.map(ProvinceEntity::asExternalModel) })
	}.flowOn(dispatchers.io)

	override fun observerCityList(provinceName: String): Flow<List<City>> {
		return cityDao.observerCityList(provinceName)
			.map { it.map(CityEntity::asExternalModel) }
	}

	override suspend fun saveFavoriteCity(city: City) {
		val favoriteCityEntity = FavoriteCityEntity(
			cityId = city.id,
			cityName = city.name,
			provinceName = city.provinceName
		)
		favoriteDao.insertFavoriteCity(favoriteCityEntity)
	}
}

