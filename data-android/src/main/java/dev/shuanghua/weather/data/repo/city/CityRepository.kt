package dev.shuanghua.weather.data.repo.city

import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.shared.Result

class CityRepository(private val remoteDataSource: CityRemoteDataSource) {

    suspend fun requestCityIdByKeyWords(param: String): String {
        return remoteDataSource.requestCityId(param)
    }

    suspend fun getCityListByProvinceId(param: String): Result<List<City>> {
        return remoteDataSource.requestCityByProvinceId(param)
    }

    companion object {
        @Volatile
        private var INSTANCE: CityRepository? = null

        fun getInstance(remoteDataSource: CityRemoteDataSource): CityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CityRepository(remoteDataSource).also { INSTANCE = it }
            }
        }
    }
}

