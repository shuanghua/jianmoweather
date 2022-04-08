package jianmoweather.data.repo.province

import com.moshuanghua.jianmoweather.shared.Result
import com.moshuanghua.jianmoweather.shared.safeApiCall
import com.moshuanghua.jianmoweather.shared.util.toJsonString
import jianmoweather.data.db.entity.City
import jianmoweather.data.db.entity.Province
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.network.outerRequestParamsMap
import java.io.IOException

class ProvinceRemoteDataSource(private val service: ShenZhenService) {

    suspend fun loadProvince() = safeApiCall(
        call = { requestProvince() },
        errorMessage = "获取省份数据出错"
    )

    suspend fun loadCityByKeyword(
        keywords: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ): Result<MutableList<City>> = safeApiCall(
        call = { requestCityByKeyword(keywords, locationCityName, district, lat, lon) },
        errorMessage = "关键字查找失败"
    )

    /**
     * 网络：获取所有省份数据
     * 缓存本地
     * Worker Thread
     */
    private suspend fun requestProvince(): Result<List<Province>> {
        val response = service.getProvinceAsync()
        val result = response.body()?.data?.list
        return if (response.body()?.data != null && !result.isNullOrEmpty()) {
            Result.Success(result)
        } else {
            Result.Error(IOException("获取省份数据出错: 没有数据"))
        }
    }

    /**
     * 关键字查询城市ID, 城市查找页面用到
     */
    private suspend fun requestCityByKeyword(
        keywords: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ): Result<MutableList<City>> {
        val paramMap = buildShenZhenProvinceParamsMap(keywords)
        val requestMap =
            outerRequestParamsMap(
                paramMap,
                locationCityName,
                district,
                lat,
                lon
            )
        val url = requestMap.toJsonString()
        val response = service.getCityByKeywordsAsync(url)
        val result = response.body()?.data?.list
        return if (response.body()?.data != null && !result.isNullOrEmpty()) {
            Result.Success(result)
        } else {
            Result.Error(IOException("该省份下没有城市: key = $keywords"))
        }
    }


    private fun buildShenZhenProvinceParamsMap(keywords: String): Map<String, String> {
        return mapOf("ids" to "", "key" to keywords)
    }
}