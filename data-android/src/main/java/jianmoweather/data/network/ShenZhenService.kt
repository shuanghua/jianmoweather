package jianmoweather.data.network

import jianmoweather.data.repo.weather.WeatherRemoteDataSource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ShenZhenService {
    @POST("phone/api/IndexV41.do")
    suspend fun getWeather(@Query("data") data: String): Response<ShenZhen<WeatherData>>

    @GET("phone/api/ProvinceList.do?data={}")
    suspend fun getProvinceAsync(): Response<ShenZhen<ProvinceList>>

    @GET("phone/api/ProvinceCityList.do")
    suspend fun getCityByProvinceIdAsync(@Query("data") data: String): Response<ShenZhen<CityList>>

    @GET("phone/api/FindCityList.do")
    suspend fun getCityByKeywordsAsync(@Query("data") data: String): Response<ShenZhen<CityList>>

    @GET("phone/api/AutoStationList.do")
    suspend fun getStationAsync(@Query("data") data: String): Response<ShenZhen<StationList>>

    companion object {
        var LOC_CITY_ID: String = ""

        //http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data=
        //http://szqxapp1.121.com.cn/hone/api/FindCityList.do?data=
        const val BASE_URL = "http://szqxapp1.121.com.cn/"
        const val TYPHOON = "http://szqxapp.121.com.cn:8081/phone/app/webPage/typhoon/typhoon.html"
        const val SATELLITE = "http://szmbapp.121.com.cn:8081/phone/app/webPage/satellite.html"
        const val ICON_HOST = "http://szqxapp.121.com.cn:8081/webcache/appimagesnew/"

        var AQI_WEB = "http://szqxapp.121.com.cn:8081/phone/api/AqiWeb.web?cityid=28060159493"
    }
}

fun outerRequestParamsMap(
    paramsMap: Map<String, String>,
    cityName: String,
    district: String,
    lat: String,
    lon: String
): Map<String, Any> {
    return mapOf(
        "pcity" to cityName,
        "parea" to district,
        "lon" to lon,
        "lat" to lat,
        "uid" to WeatherRemoteDataSource.UID,
        "os" to WeatherRemoteDataSource.OS,
        "ver" to WeatherRemoteDataSource.VER,
        "net" to WeatherRemoteDataSource.NET,
        "type" to WeatherRemoteDataSource.TYPE,
        "token" to WeatherRemoteDataSource.TOKEN,
        "rever" to WeatherRemoteDataSource.REVER,
        "uname" to WeatherRemoteDataSource.UNAME,
        "Param" to paramsMap
    )
}





