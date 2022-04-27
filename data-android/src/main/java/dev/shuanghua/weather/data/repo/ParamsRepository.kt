package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.shared.util.toJsonString
import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.network.*
import dev.shuanghua.weather.data.repo.city.CityRemoteDataSource

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
        } else {  // 当数据库没有时,使用 APP 默认
            getLocationCityWeatherRequestJson(MainWeatherParam.DEFAULT)
        }
    }

//    suspend fun createRequestJson(outerParam: OuterParam, innerParam: Param): String {
//        val innerMap = when (innerParam) {
//            is MainWeatherParam -> createMainWeatherInnerParam(innerParam)
//            is FavoriteWeatherParam -> createFavoriteWeatherInnerParam(innerParam)
//            else -> null
//        } ?: throw Throwable("请求参数不能为空!")
//        return createFullRequestParam(outerParam, innerMap)
//    }


    suspend fun getLocationCityWeatherRequestJson(innerParam: MainWeatherParam): String {
        paramsDao.insertLocationCityWeatherRequestParam(outerParam, innerParam)
        // 先转 map, 然后转 json
        val innerMap = createMainWeatherInnerParam(innerParam)
        val outerMap = createOuterParam(outerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }


    suspend fun updateCityIds(cityIdList: List<String>) {
        cityIds = cityIdList.joinToString()
    }

    suspend fun getFavoriteWeatherRequestJson(cityIds:String): String {
        val innerParam = FavoriteWeatherParam(
            lon = outerParam.lon,
            lat = outerParam.lat,
            isauto = "0",
            cityids = cityIds
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
    private suspend fun createOuterParam(
        outerParam: OuterParam
    ): MutableMap<String, Any> {
        return mutableMapOf(
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
            "os" to outerParam.os,
            "Param" to ""
        )
    }

    /**
     * 首页定位城市城市请求参数
     */
    private suspend fun createMainWeatherInnerParam(
        param: MainWeatherParam
    ): Map<String, String> {
        return mapOf(
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
    }

    /**
     * 收藏页面天气请求参数
     */
    private suspend fun createFavoriteWeatherInnerParam(param: FavoriteWeatherParam): Map<String, String> {
        return mapOf(
            "isauto" to param.isauto,
            "cityids" to param.cityids,
            "lon" to param.lon,
            "lat" to param.lat,
        )
    }

    /**
     * 根据城市名字查询对应的 cityId
     */
    private fun createGetCityIdByKeyWordsInnerParam(
        innerParam: FindCitIdyByKeyWordsParam
    ): Map<String, String> {
        return mapOf(
            "key" to innerParam.key,
            "cityids" to innerParam.cityids,
        )
    }

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

