package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.entity.CityEntity
import dev.shuanghua.weather.data.db.entity.Province
import dev.shuanghua.weather.data.model.DistrictWithStation
import dev.shuanghua.weather.data.model.FavoriteCityWeather
import dev.shuanghua.weather.data.model.ShenZhenNetworkWeather
import dev.shuanghua.weather.data.network.ShenZhenWeatherApi
import javax.inject.Inject

interface SZNetworkDataSource {
    suspend fun getMainWeather(params: String): ShenZhenNetworkWeather?

    suspend fun getFavoriteCityWeather(params: String): List<FavoriteCityWeather>?

    suspend fun getProvinces(): List<Province>?

    suspend fun getProvinceCityList(params: String): List<CityEntity>?

    suspend fun getDistrictWithStationList(params: String): List<DistrictWithStation>?
}


class RetrofitWeatherRepository @Inject constructor(
    private val szApi: ShenZhenWeatherApi,
) : SZNetworkDataSource {
    override suspend fun getMainWeather(params: String): ShenZhenNetworkWeather? =
        szApi.getMainWeather(params).body()?.data

    override suspend fun getFavoriteCityWeather(params: String): List<FavoriteCityWeather>? =
        szApi.getFavoriteCityWeather(params).body()?.data?.list

    override suspend fun getProvinces(): List<Province>? =
        szApi.getProvinces().body()?.data?.list

    override suspend fun getProvinceCityList(params: String): List<CityEntity>? =
        szApi.getProvinceCityList(params).body()?.data?.list

    override suspend fun getDistrictWithStationList(params: String): List<DistrictWithStation>? =
        szApi.getDistrictWithStationList(params).body()?.data?.list
}
