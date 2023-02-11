package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.ProvinceDao
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.network.SzwNetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ShenZhenProvince
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.data.android.repository.converter.asWeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProvinceRepository @Inject constructor(
    private val provinceDao: ProvinceDao,
    private val network: SzwNetworkDataSource
) {
    suspend fun updateProvince() {
        val provinceList: List<ShenZhenProvince> = network.getProvinceList()
        provinceDao.insertProvince(provinceList.map { it.asWeatherEntity() })
    }

    fun observerProvinces(): Flow<List<Province>> =
        provinceDao.observerProvinces().map { provinceEntityList ->
            provinceEntityList.map { it.asExternalModel() }
        }

//    companion object {
//        @Volatile
//        private var INSTANCE: ProvinceRepository? = null
//
//        fun getInstance(
//            provinceDao: ProvinceDao,
//            service: ShenZhenRetrofitApi
//        ): ProvinceRepository {
//            return INSTANCE
//                ?: synchronized(this) {
//                    INSTANCE
//                        ?: ProvinceRepository(
//                            provinceDao,
//                            service
//                        ).also { INSTANCE = it }
//                }
//        }
//    }
}

