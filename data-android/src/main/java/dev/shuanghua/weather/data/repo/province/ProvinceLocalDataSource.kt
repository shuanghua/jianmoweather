package dev.shuanghua.weather.data.repo.province

import dev.shuanghua.weather.data.db.dao.ProvinceDao
import dev.shuanghua.weather.data.db.entity.Province
import kotlinx.coroutines.flow.Flow

class ProvinceLocalDataSource(private val provinceDao: ProvinceDao) {

    suspend fun insertProvinces(provinces: List<Province>) = provinceDao.insertProvince(provinces)

    fun observerProvinces(): Flow<List<Province>> = provinceDao.observerProvinces()

    companion object {
        @Volatile
        private var INSTANCE: ProvinceLocalDataSource? = null

        fun getInstance(
            provinceDao: ProvinceDao
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ProvinceLocalDataSource(
                provinceDao
            ).also { INSTANCE = it }
        }
    }
}