package dev.shuanghua.weather.data.repo.city

import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.network.ShenZhenService

class CityRemoteDataSource(private val service: ShenZhenService) {

    suspend fun requestCityByProvinceId(param: String): Result<List<City>> {
        val response = service.getCityByProvinceIdAsync(param)
        val result = response.body()?.data?.list
        return if (response.body()?.data != null && !result.isNullOrEmpty()) {
            Result.Success(result)
        } else {
            Result.Error(Exception("该省份下没有城市"))
        }
    }

    /**
     * 根据城市名字查询城市id
     * 用在定位城市查询
     * 返回 cityId 或者抛出异常
     */
    suspend fun requestCityId(param: String): String {
        val response = service.getCityByKeywordsAsync(param)
        val list = response.body()?.data?.list
        return if (!list.isNullOrEmpty()) list[0].id else throw Exception("服务器没有该城市ID")

    }

}