package dev.shuanghua.weather.data.repo.city

import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.network.ShenZhenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CityRepository(private val service: ShenZhenService) {

    suspend fun requestCityIdByKeyWords(param: String): String {
        val response = service.getCityByKeywordsAsync(param)
        val list = response.body()?.data?.list
        return if (!list.isNullOrEmpty()) list[0].id else throw Exception("服务器没有该城市ID")
    }

    suspend fun getCityListByProvinceId(param: String): List<City> {
        return service.getCityByProvinceIdAsync(param).body()?.data?.list!!
    }


    companion object {
        @Volatile
        private var INSTANCE: CityRepository? = null

        fun getInstance(service: ShenZhenService): CityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CityRepository(service).also { INSTANCE = it }
            }
        }
    }
}

