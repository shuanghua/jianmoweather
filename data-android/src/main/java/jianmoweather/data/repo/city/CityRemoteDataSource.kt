package jianmoweather.data.repo.city

import com.moshuanghua.jianmoweather.shared.Result
import com.moshuanghua.jianmoweather.shared.safeApiCall
import com.moshuanghua.jianmoweather.shared.util.toJsonString
import jianmoweather.data.db.entity.City
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.network.outerRequestParamsMap
import timber.log.Timber

class CityRemoteDataSource(private val service: ShenZhenService) {
    /**
     * 网络：根据省份ID查找城市
     * 不缓存本地
     * Worker Thread
     */
    suspend fun loadCityByProvinceId(
        provinceId: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ) = safeApiCall(
        call = { requestCityByProvince(provinceId, locationCityName, district, lat, lon) },
        errorMessage = "根据省份ID获取城市出错"
    )

    private suspend fun requestCityByProvince(
        provinceId: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ): Result<MutableList<City>> {
        val paramMap = buildShenZhenCityIdParamsMap(provinceId)
        val requestMap =
            outerRequestParamsMap(
                paramMap,
                locationCityName,
                district,
                lat,
                lon
            )
        val url = requestMap.toJsonString()
        val response = service.getCityByProvinceIdAsync(url)
        val result = response.body()?.data?.list
        return if(response.body()?.data != null && !result.isNullOrEmpty()) {
            Result.Success(result)
        } else {
            Result.Error(Exception("该省份下没有城市: provinceId = $provinceId"))
        }
    }

    /**
     * 根据城市名字查询城市id
     * 用在定位城市查询
     * 返回 cityId 或者抛出异常
     */
    suspend fun fetchCityId(
        cityName: String,
        latitude: String,
        longitude: String,
        district: String
    ): String {
        val keywords = cityName.substringBefore("市")
        val paramMap = buildShenZhenCityKeyParamsMap(keywords)
        val requestMap =
            outerRequestParamsMap(
                paramMap,
                cityName,
                district,
                latitude,
                longitude
            )
        val url = requestMap.toJsonString()
        val response = service.getCityByKeywordsAsync(url)
        val list = response.body()?.data?.list
        return if(!list.isNullOrEmpty()) {
            Timber.d("-----------------------请求城市id-----")
            list[0].cityId
        } else {
            throw Exception("服务器没有该城市id: $cityName")
        }
    }

    private fun buildShenZhenCityIdParamsMap(provinceId: String): Map<String, String> {
        return mapOf("cityIds" to "", "provId" to provinceId)
    }

    private fun buildShenZhenCityKeyParamsMap(key: String): Map<String, String> {
        return mapOf("cityids" to "", "key" to key)//"{"cityids":"","key:"$key"}"
    }
}