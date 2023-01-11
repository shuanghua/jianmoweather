package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.api.ShenZhenRetrofitApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenCity
import dev.shuanghua.weather.data.android.network.model.ShenZhenDistrict
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.network.model.ShenZhenProvince
import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import javax.inject.Inject

interface NetworkDataSource {
    suspend fun getMainWeather(params: String): ShenZhenWeather?

    suspend fun getFavoriteCityWeather(params: String): List<ShenZhenFavoriteCityWeather>

    suspend fun getProvinces(): List<ShenZhenProvince>?

    suspend fun getCityList(params: String): List<ShenZhenCity>?

    suspend fun getDistrictWithStationList(params: String): List<ShenZhenDistrict>?
}


class RetrofitNetworkDataSource @Inject constructor(
    private val szApi: ShenZhenRetrofitApi,
) : NetworkDataSource {

    override suspend fun getMainWeather(params: String): ShenZhenWeather? {
        return szApi.getMainWeather(params).body()?.data
    }

    override suspend fun getFavoriteCityWeather(params: String): List<ShenZhenFavoriteCityWeather> =
        szApi.getFavoriteCityWeather(params).body()?.data?.list!!

    override suspend fun getDistrictWithStationList(params: String): List<ShenZhenDistrict>? =
        szApi.getDistrictWithStationList(params).body()?.data?.list

    override suspend fun getProvinces(): List<ShenZhenProvince>? =
        szApi.getProvinces().body()?.data?.list

    override suspend fun getCityList(params: String): List<ShenZhenCity>? =
        szApi.getCityList(params).body()?.data?.cityList
}
