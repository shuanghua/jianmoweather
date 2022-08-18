package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.network.*
import dev.shuanghua.weather.shared.util.toJsonString

/**
 * 定位 + 查询城市id
 */
class ParamsRepository(private val paramsDao: ParamsDao) {

    var parentParamWithLocation = ParentParam()  //在天气页面，定位成功时赋值
    var parentParam = ParentParam()// 用于非定位请求天气的情况
    var cityIds: String = "28060159493"
        private set

    /**
     * 天气
     */
    fun getWeatherJson(
        outerParam: ParentParam,
        innerParam: WeatherParam
    ): String {
//        paramsDao.insertLocationCityWeatherRequestParam(outerParam, innerParam)
        val innerMap = createMainWeatherInnerParam(innerParam) // 先转 map, 然后转 json
        val outerMap = createOuterParam(outerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    /**
     * Query City ID
     */
    fun getCityIdJson(innerParam: QueryCityIdParam): String {
        val outerMap = createOuterParam(parentParamWithLocation)
        val innerMap = createGetCityIdByKeyWordsInnerParam(innerParam)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    /**
     * CityList
     */
    fun getCityListByProvinceIdJson(provinceId: String): String {
        val outerMap = createOuterParam(parentParamWithLocation) // 外部param使用通用的pram
        val innerMap = createProvinceInnerParam(CityListParam(provinceId, cityIds))
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    /**
     * District
     */
    fun getDistrictParam(param: DistrictParam): String {
        val innerMap = createDistrictInnerParam(param)
        val outerMap = createOuterParam(parentParam) // 外部param使用通用的pram
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Favorite
     */
    fun getFavoriteParam(cityIdList: ArrayList<String>): String {
        this.cityIds = cityIdList.joinToString(separator = ",")
        val innerParam = FavoriteParam(isauto = "1", cityids = this.cityIds)
        val innerMap = createFavoriteWeatherInnerParam(innerParam)
        val outerMap = createOuterParam(parentParamWithLocation)
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    private fun createProvinceInnerParam(
        param: CityListParam
    ): MutableMap<String, Any> = mutableMapOf(
        "provId" to param.provId,
        "cityids" to param.cityids,
    )

    private fun createDistrictInnerParam(
        param: DistrictParam
    ): MutableMap<String, Any> = mutableMapOf(
        "cityid" to param.cityid,
        "obtid" to param.obtid,
    )

    /**
     * 首页定位城市城市请求参数
     */
    private fun createMainWeatherInnerParam(
        param: WeatherParam
    ) = mapOf(
        "cityid" to param.cityid,
        "obtid" to param.obtId,
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
        param: FavoriteParam
    ) = mapOf(
        "isauto" to param.isauto,
        "cityids" to param.cityids,
    )


    /**
     * 根据城市名字查询对应的 cityId
     */
    private fun createGetCityIdByKeyWordsInnerParam(
        innerParam: QueryCityIdParam
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

    /**
     * 通用外部 Param
     */
    private fun createOuterParam(
        outerParam: ParentParam
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
}

