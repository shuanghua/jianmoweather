package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.model.CityReturn
import dev.shuanghua.weather.data.android.network.model.DistrictReturn
import dev.shuanghua.weather.data.android.network.model.FavoriteCityWeatherReturn
import dev.shuanghua.weather.data.android.network.model.ProvinceReturn
import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.SzwModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ApiServiceTest {
    @GET("phone/api/IndexV41.do")
    suspend fun getMainWeather(
        @Query("data") data: String,
    ): ShenZhenReturnData<SzwModel>

    @GET("phone/api/AlreadyAddCityList.do")
    suspend fun getFavoriteCityWeather(
        @Query("data") data: String,
    ): ShenZhenReturnData<FavoriteCityWeatherReturn>

    @GET("phone/api/ProvinceList.do?data={}")
    suspend fun getProvinces(): ShenZhenReturnData<ProvinceReturn>

    @GET("phone/api/ProvinceCityList.do")
    suspend fun getCityList(
        @Query("data") data: String,
    ): ShenZhenReturnData<CityReturn>

    @GET("phone/api/FindCityList.do")
    suspend fun getCityByKeywordsAsync(
        @Query("data") data: String,
    ): ShenZhenReturnData<CityReturn>

    @GET("phone/api/AutoStationList.do")
    suspend fun getDistrictWithStationList(
        @Query("data") data: String,
    ): ShenZhenReturnData<DistrictReturn>
}


