package dev.shuanghua.weather.data.android.network.api

import dev.shuanghua.weather.data.android.network.model.CityReturn
import dev.shuanghua.weather.data.android.network.model.DistrictReturn
import dev.shuanghua.weather.data.android.network.model.FavoriteCityWeatherReturn
import dev.shuanghua.weather.data.android.network.model.ProvinceReturn
import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApi
 */
interface ShenZhenApi {
    @GET("phone/api/IndexV41.do")
    suspend fun getMainWeather(
        @Query("data") data: String,
    ): ShenZhenReturnData<ShenZhenWeather>

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

    companion object {  //TODO("自行获取对应接口")
        const val BASE_URL = Api.BASE_URL
        const val IMAGE_URL = Api.IMAGE_URL
        const val AQI_IMAGE_URL = Api.AQI_IMAGE_URL
        const val AQI_WEB_URL = Api.AQI_WEB_URL
    }
}

// @Query
// 会自动添加 ？= & 符号
// phone/api/IndexV41.do?name=值
/*
@POST("phone/api/IndexV41.do")
suspend fun getMainWeather(
    @Query("name") value: String,
)
*/

// @Query 注解会把括号内的 字段名name 和 对应的值data，先用=号拼接, 也就是 name="data"
// 同时在 "phone/api/IndexV41.do 末尾添加 ? 号
// 最后把 name="data" 放到 ? 号后面拼接 phone/api/IndexV41.do?name=值
// 如果有多个 @Query ，则对 Query 使用 & 符合来拼接多个 Query，如 name=张三&age=100,然后再添加到 ? 后面
// 也就是说 Query 会自动添加 ？= & 符号


// @QueryMap
// Url 中有多个 ? 时使用


// @Path
// github/user/{userId}


