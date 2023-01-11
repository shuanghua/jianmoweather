package dev.shuanghua.weather.data.android.network.api

import dev.shuanghua.weather.data.android.network.model.CityReturn
import dev.shuanghua.weather.data.android.network.model.DistrictReturn
import dev.shuanghua.weather.data.android.network.model.FavoriteCityWeatherReturn
import dev.shuanghua.weather.data.android.network.model.ProvinceReturn
import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ShenZhenRetrofitApi {
    @POST("phone/api/IndexV41.do")
    suspend fun getMainWeather(@Query("data") data: String): Response<ShenZhenReturnData<ShenZhenWeather>>

    @POST("phone/api/AlreadyAddCityList.do")
    suspend fun getFavoriteCityWeather(@Query("data") data: String): Response<ShenZhenReturnData<FavoriteCityWeatherReturn>>

    @GET("phone/api/ProvinceList.do?data={}")
    suspend fun getProvinces(): Response<ShenZhenReturnData<ProvinceReturn>>

    @POST("phone/api/ProvinceCityList.do")
    suspend fun getCityList(@Query("data") data: String): Response<ShenZhenReturnData<CityReturn>>

    @POST("phone/api/FindCityList.do")
    suspend fun getCityByKeywordsAsync(@Query("data") data: String): Response<ShenZhenReturnData<CityReturn>>

    @POST("phone/api/AutoStationList.do") //http://szqxapp1.121.com.cn/phone/api/AutoStationList.do?data=
    suspend fun getDistrictWithStationList(@Query("data") data: String): Response<ShenZhenReturnData<DistrictReturn>>


    companion object {
        //http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data=
        //http://szqxapp1.121.com.cn/hone/api/FindCityList.do?data=

        //收藏页面
        //http://szqxapp1.121.com.cn:80/phone/api/AlreadyAddCityList.do?data={"type":"1","ver":"v5.7.0","rever":"578","net":"WIFI","pcity":"深圳市","parea":"宝安区","lon":"113.81035121710433","lat":"22.760361451345034","gif":"true","uid":"Rjc4qedi323eK4PGsztqsztq","uname":"","token":"","os":"android30","Param":{"isauto":"1","cityids":"28060159493,32010145005,28010159287,02010058362,01010054511,30120659033","lon":"113.81035121710433","lat":"22.760361451345034"}}

        const val BASE_URL = "http://szqxapp1.121.com.cn/"
        const val TYPHOON = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html"
        const val SATELLITE = "http://szmbapp1.121.com.cn:80/phone/app/webPage/satellite.html"
        const val ICON_HOST = "http://szqxapp1.121.com.cn:80/webcache/appimagesnew/"
        //http://szqxapp1.121.com.cn/webcache/appimagesnew/bgSmall/city_smallBgn7_1.png
        //http://szqxapp1.121.com.cn/webcache/appimagesnew/bgSmall/sz_city_smallBgn3_1.png

        var AQI_WEB = "http://szqxapp1.121.com.cn:80/phone/api/AqiWeb.web?cityid=28060159493"
    }
}





