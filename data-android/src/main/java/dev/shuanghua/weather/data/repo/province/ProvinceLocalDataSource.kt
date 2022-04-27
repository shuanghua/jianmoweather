package dev.shuanghua.weather.data.repo.province

import dev.shuanghua.weather.data.db.dao.CityDao
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.db.entity.Province

class ProvinceLocalDataSource(private val cityDao: CityDao) {

    suspend fun insertProvinces(provinces: List<Province>) = cityDao.insertProvince(provinces)

    suspend fun queryProvince(): List<City> = cityDao.findProvinces()

    companion object {
        @Volatile
        private var INSTANCE: ProvinceLocalDataSource? = null

        fun getInstance(
            cityDao: CityDao
        ): ProvinceLocalDataSource {
            return INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: ProvinceLocalDataSource(
                        cityDao
                    ).also { INSTANCE = it }
            }
        }
    }
}