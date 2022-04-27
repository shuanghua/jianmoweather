package dev.shuanghua.weather.data.repo.province

import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.safeApiCall
import dev.shuanghua.weather.data.db.entity.Province
import dev.shuanghua.weather.data.network.ShenZhenService
import java.io.IOException

class ProvinceRemoteDataSource(private val service: ShenZhenService) {

    suspend fun loadProvince() = safeApiCall(
        call = { requestProvince() },
        errorMessage = "获取省份数据出错"
    )

    /**
     * 获取所有省份
     * 无需请求参数
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
}