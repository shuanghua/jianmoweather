package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.db.entity.OuterParam
import dev.shuanghua.weather.data.db.entity.WeatherParam
import dev.shuanghua.weather.data.network.FavoriteWeatherParam
import dev.shuanghua.weather.data.network.FindCitIdyByKeyWordsParam
import dev.shuanghua.weather.shared.util.toJsonString

/**
 * 定位 + 查询城市id
 */
class ParamsRepository(private val paramsDao: ParamsDao) {

    private var outerParam: OuterParam = OuterParam()
    var cityIds: String = ""
        private set

    fun updateOuterParam(
        cityName: String,
        district: String,
        longitude: String,
        latitude: String
    ): OuterParam {
        val newOuterParam = OuterParam(
            pcity = cityName,
            parea = district,
            lon = longitude,
            lat = latitude
        )
        this.outerParam = newOuterParam
        return outerParam
    }

    suspend fun getLastLocationCityWeatherParam(): String {
        val lastRequestParams = paramsDao.getLastLocationCityWeatherRequestParam()
        return if (
            lastRequestParams.lastOuterParam != null &&
            lastRequestParams.lastMainWeatherParam !== null
        ) {
            outerParam = lastRequestParams.lastOuterParam
            getLocationCityWeatherRequestJson(lastRequestParams.lastMainWeatherParam)
        } else {  // 当数据库没有时, 使用 APP 默认
            getLocationCityWeatherRequestJson(WeatherParam.DEFAULT)
        }
    }

    suspend fun getLocationCityWeatherRequestJson(innerParam: WeatherParam): String {
        paramsDao.insertLocationCityWeatherRequestParam(outerParam, innerParam)
        val innerMap = createMainWeatherInnerParam(innerParam) // 先转 map, 然后转 json
        val outerMap = createOuterParam(outerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    suspend fun updateCityIds(cityIdList: ArrayList<String>) {
        cityIds = cityIdList.joinToString(separator = ",")
    }

    suspend fun getFavoriteWeatherRequestJson(cityIdList: ArrayList<String>): String {
        this.cityIds = cityIdList.joinToString(separator = ",")

        val innerParam = FavoriteWeatherParam(
            lon = outerParam.lon,
            lat = outerParam.lat,
            isauto = "0",
            cityids = this.cityIds
        )

        val innerMap = createFavoriteWeatherInnerParam(innerParam)
        val outerMap = createOuterParam(this.outerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    suspend fun getCityIdByKeyWordsRequestJson(keyWords: String): String {
        val cityName = keyWords.substringBefore("市")
        val innerParam = FindCitIdyByKeyWordsParam(key = cityName, cityids = cityIds)
        val innerMap = createGetCityIdByKeyWordsInnerParam(innerParam)
        val outerMap = createOuterParam(this.outerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    suspend fun getStationListRequestJson() {

    }


    // -------------------------------------------------------------------------------------------------
    private fun createOuterParam(
        outerParam: OuterParam
    ): MutableMap<String, Any> = mutableMapOf(
        "type" to outerParam.type,
        "ver" to outerParam.ver,
        "rever" to outerParam.rever,
        "net" to outerParam.net,
        "pcity" to outerParam.pcity,
        "parea" to outerParam.parea,
        "lon" to outerParam.lon,
        "lat" to outerParam.lat,
        "gif" to outerParam.gif,
        "uid" to outerParam.uid,
        "uname" to outerParam.uname,
        "token" to outerParam.token,
        "os" to outerParam.os
    )


    /**
     * 首页定位城市城市请求参数
     */
    private fun createMainWeatherInnerParam(
        param: WeatherParam
    ) = mapOf(
        "cityid" to param.cityid,
        "isauto" to param.isauto,
        "w" to param.w,
        "h" to param.h,
        "cityids" to param.cityids,
        "pcity" to param.pcity,
        "parea" to param.parea,
        "lon" to param.lon,
        "lat" to param.lat,
        "gif" to param.gif
    )

    /**
     * 收藏页面天气请求参数
     */
    private fun createFavoriteWeatherInnerParam(
        param: FavoriteWeatherParam
    ) = mapOf(
        "isauto" to param.isauto,
        "cityids" to param.cityids,
        "lon" to param.lon,
        "lat" to param.lat,
    )


    /**
     * 根据城市名字查询对应的 cityId
     */
    private fun createGetCityIdByKeyWordsInnerParam(
        innerParam: FindCitIdyByKeyWordsParam
    ) = mapOf(
        "key" to innerParam.key,
        "cityids" to innerParam.cityids,
    )


    companion object {
        @Volatile
        private var INSTANCE: ParamsRepository? = null
        fun getInstance(paramsDao: ParamsDao): ParamsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ParamsRepository(paramsDao).also { INSTANCE = it }
            }
        }
    }
}

