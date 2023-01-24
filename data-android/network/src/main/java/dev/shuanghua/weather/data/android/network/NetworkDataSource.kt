package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenCity
import dev.shuanghua.weather.data.android.network.model.ShenZhenDistrict
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.network.model.ShenZhenProvince
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NetworkDataSource {
    suspend fun getMainWeather(
        params: String
    ): ShenZhenWeather

    suspend fun getFavoriteCityWeatherList(
        params: String
    ): List<ShenZhenFavoriteCityWeather>

    suspend fun getProvinceList(): List<ShenZhenProvince>

    suspend fun getCityList(
        params: String
    ): List<ShenZhenCity>

    suspend fun getDistrictWithStationList(
        params: String
    ): List<ShenZhenDistrict>?
}


class RetrofitNetworkDataSource @Inject constructor(
    private val szApi: ShenZhenApi,
    private val dispatcher: AppCoroutineDispatchers,
) : NetworkDataSource {

    override suspend fun getMainWeather(
        params: String
    ): ShenZhenWeather =
        withContext(dispatcher.io) {
            szApi.getMainWeather(params).data
        }

    override suspend fun getFavoriteCityWeatherList(
        params: String
    ): List<ShenZhenFavoriteCityWeather> =
        withContext(dispatcher.io) {
            szApi.getFavoriteCityWeather(params).data.list
        }

    /**
     * 服务器上，非广东城市的站点列表数据为 null
     */
    override suspend fun getDistrictWithStationList(
        params: String
    ): List<ShenZhenDistrict>? =
        withContext(dispatcher.io) {
            szApi.getDistrictWithStationList(params).data.list
        }

    override suspend fun getProvinceList(): List<ShenZhenProvince> =
        withContext(dispatcher.io) {
            szApi.getProvinces().data.list
        }

    override suspend fun getCityList(
        params: String
    ): List<ShenZhenCity> =
        withContext(dispatcher.io) {
            szApi.getCityList(params).data.cityList
        }
}
