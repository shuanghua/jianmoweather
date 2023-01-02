package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.entity.CityEntity

class CityRepository(private val szNetworkDataSource: SZNetworkDataSource) {

//    suspend fun requestCityIdByKeyWords(param: String): String {
//        val response = szNetworkDataSource.getCityByKeywordsAsync(param)
//        val list = response.body()?.data?.list
//        return if (!list.isNullOrEmpty()) list[0].id else throw Exception("服务器没有该城市ID")
//    }

    suspend fun getCityListByProvinceId(params: String): List<CityEntity>? {
        return szNetworkDataSource.getProvinceCityList(params)
    }


    companion object {
        @Volatile
        private var INSTANCE: CityRepository? = null

        fun getInstance(
            szNetworkDataSource: SZNetworkDataSource
        ): CityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CityRepository(szNetworkDataSource).also { INSTANCE = it }
            }
        }
    }
}

