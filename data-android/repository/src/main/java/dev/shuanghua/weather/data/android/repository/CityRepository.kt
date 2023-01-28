package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.CityDao
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.data.android.repository.convert.asWeatherEntity
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao,
    private val network: NetworkDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {

    fun observerCityList(provinceName: String): Flow<List<City>> {
        return cityDao.observerCityList(provinceName)
            .map { it.map(CityEntity::asExternalModel) }
    }

    /**
     * @provinceName 保存数据库时作为主键
     */
    suspend fun updateCityList(
        params: CityListParams,
        provinceName: String
    ) = withContext(dispatchers.io) {
        val cityList = network.getCityList(params)
        val cityEntityList = cityList.map { it.asWeatherEntity(provinceName) }
        cityDao.insertCityList(cityEntityList)
    }

}

