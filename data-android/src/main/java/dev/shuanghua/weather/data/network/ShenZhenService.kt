package dev.shuanghua.weather.data.network

import dev.shuanghua.weather.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ShenZhenService {
    @POST("phone/api/IndexV41.do")
    suspend fun getWeather(@Query("data") data: String): Response<ShenZhenCommon<MainReturn>>

    @POST("phone/api/AlreadyAddCityList.do")
    suspend fun getFavoriteCityWeather(@Query("data") data: String): Response<ShenZhenCommon<FavoriteReturn>>

    @GET("phone/api/ProvinceList.do?data={}")
    suspend fun getProvince(): Response<ShenZhenCommon<ProvinceReturn>>

    //http://szqxapp1.121.com.cn/phone/api/ProvinceCityList.do?data={"type":"1","ver":"v5.7.0","rever":"578","net":"WIFI","pcity":"","parea":"","lon":"","lat":"","gif":"true","uid":"Rjc4qedi323eK4PGsztqsztq","uname":"","token":"","os":"android30","Param":{"provId":"01","cityids":""}}

    @GET("phone/api/ProvinceCityList.do")
    suspend fun getCityByProvinceIdAsync(@Query("data") data: String): Response<ShenZhenCommon<CityReturn>>

    @GET("phone/api/FindCityList.do")
    suspend fun getCityByKeywordsAsync(@Query("data") data: String): Response<ShenZhenCommon<CityReturn>>

    @GET("phone/api/AutoStationList.do")
    suspend fun getStationList(@Query("data") data: String): Response<ShenZhenCommon<StationReturn>>


    companion object {
        //http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data=
        //http://szqxapp1.121.com.cn/hone/api/FindCityList.do?data=

        //收藏页面
        //http://szqxapp1.121.com.cn:80/phone/api/AlreadyAddCityList.do?data={"type":"1","ver":"v5.7.0","rever":"578","net":"WIFI","pcity":"深圳市","parea":"宝安区","lon":"113.81035121710433","lat":"22.760361451345034","gif":"true","uid":"Rjc4qedi323eK4PGsztqsztq","uname":"","token":"","os":"android30","Param":{"isauto":"1","cityids":"28060159493,32010145005,28010159287,02010058362,01010054511,30120659033","lon":"113.81035121710433","lat":"22.760361451345034"}}

        const val BASE_URL = "http://szqxapp1.121.com.cn/"
        const val TYPHOON = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html"
        const val SATELLITE = "http://szmbapp1.121.com.cn:80/phone/app/webPage/satellite.html"
        const val ICON_HOST = "http://szqxapp1.121.com.cn:80/webcache/appimagesnew/"

        var AQI_WEB = "http://szqxapp1.121.com.cn:80/phone/api/AqiWeb.web?cityid=28060159493"
    }
}





