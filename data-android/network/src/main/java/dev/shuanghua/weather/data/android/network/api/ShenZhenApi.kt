package dev.shuanghua.weather.data.android.network.api

import dev.shuanghua.weather.data.android.network.model.CityReturn
import dev.shuanghua.weather.data.android.network.model.DistrictReturn
import dev.shuanghua.weather.data.android.network.model.FavoriteCityWeatherReturn
import dev.shuanghua.weather.data.android.network.model.ProvinceReturn
import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ShenZhenApi {
    @POST("phone/api/IndexV41.do")
    suspend fun getMainWeather(@Query("data") data: String): ShenZhenReturnData<ShenZhenWeather>

    @POST("phone/api/AlreadyAddCityList.do")
    suspend fun getFavoriteCityWeather(@Query("data") data: String): ShenZhenReturnData<FavoriteCityWeatherReturn>

    @GET("phone/api/ProvinceList.do?data={}")
    suspend fun getProvinces(): ShenZhenReturnData<ProvinceReturn>

    @POST("phone/api/ProvinceCityList.do")
    suspend fun getCityList(@Query("data") data: String): ShenZhenReturnData<CityReturn>

    @POST("phone/api/FindCityList.do")
    suspend fun getCityByKeywordsAsync(@Query("data") data: String): ShenZhenReturnData<CityReturn>

    @POST("phone/api/AutoStationList.do")
    suspend fun getDistrictWithStationList(@Query("data") data: String): ShenZhenReturnData<DistrictReturn>

    companion object {
        const val Url_Base = "http://szqxapp1.121.com.cn/"
        const val Url_Image = "http://szqxapp1.121.com.cn:80/webcache/appimagesnew/"
        const val Url_Image_Aqi = "http://szqxapp1.121.com.cn:80/phone/images/"
        var Url_AqiWeb = "http://szqxapp1.121.com.cn:80/phone/api/AqiWeb.web?cityid="
    }
}






