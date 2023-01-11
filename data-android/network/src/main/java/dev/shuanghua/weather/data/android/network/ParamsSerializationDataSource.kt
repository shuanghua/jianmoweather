package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.shuanghua.weather.data.android.model.request.CityScreenRequest
import dev.shuanghua.weather.data.android.model.request.DistrictScreenRequest
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenCityRequest
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenStationRequest
import dev.shuanghua.weather.data.android.model.request.QueryCityIdRequest
import dev.shuanghua.weather.data.android.model.request.WeatherScreenRequest
import dev.shuanghua.weather.data.android.network.params.toMap
import javax.inject.Inject

interface RequestParamsDataSource {
    fun getMainWeatherRequestParams(params: WeatherScreenRequest): String
    fun getFavoriteStationWeatherRequestParams(params: FavoriteScreenStationRequest): String
    fun getFavoriteCityWeatherRequestParams(params: FavoriteScreenCityRequest): String
    fun getDistrictListRequestParams(params: DistrictScreenRequest): String
    fun getCityListRequestParams(params: CityScreenRequest): String
    fun getQueryCityIdRequestParams(params: QueryCityIdRequest): String
//    fun getProvinceListRequestParams(): String 省份列表页面请求不需要传递参数
}


class ParamsSerializationDataSource @Inject constructor(
    private val moshi: Moshi
) : RequestParamsDataSource {


    private fun Map<String, Any>.toJsonString(): String {
        val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
        val mapAdapter = moshi.adapter<Map<String, Any>>(type)
        return mapAdapter.toJson(this)
    }


    override fun getMainWeatherRequestParams(params: WeatherScreenRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    override fun getFavoriteStationWeatherRequestParams(params: FavoriteScreenStationRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    override fun getFavoriteCityWeatherRequestParams(params: FavoriteScreenCityRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    override fun getDistrictListRequestParams(params: DistrictScreenRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    override fun getCityListRequestParams(params: CityScreenRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    override fun getQueryCityIdRequestParams(params: QueryCityIdRequest): String {
        val innerMap = params.innerParams.toMap()
        val outerMap = params.outerParams.toMap()
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }
}