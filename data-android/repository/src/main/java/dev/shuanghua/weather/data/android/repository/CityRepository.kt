package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.CityDao
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.repository.convert.asEntity
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao,
    private val network: NetworkDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {

//    suspend fun requestCityIdByKeyWords(param: String): String {
//        val response = szNetworkDataSource.getCityByKeywordsAsync(param)
//        val list = response.body()?.data?.list
//        return if (!list.isNullOrEmpty()) list[0].id else throw Exception("服务器没有该城市ID")
//    }

    fun observerCityList(provinceName: String): Flow<List<City>> {
        return cityDao.observerCityList(provinceName)
            .map { it.map(CityEntity::asExternalModel) }
    }

    suspend fun updateCityList(
        paramsJson: String,
        provinceName: String
    ) {
        val cityList = network.getCityList(paramsJson)
        if (cityList.isNullOrEmpty()) return
        val cityEntityList = cityList.map { it.asEntity(provinceName) }
        cityDao.insertCityList(cityEntityList)
    }

    /**
     * 直接显示到 Ui 不保存数据库
     */
    fun getNetworkCityList(
        paramsJson: String,
        provinceName: String
    ): Flow<List<City>> = flow {
        network.getCityList(paramsJson)?.map { it.asExternalModel(provinceName) }
    }

}

