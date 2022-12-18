package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.network.*
import dev.shuanghua.weather.shared.util.toJsonString

/**
 * 定位 + 查询城市id
 */
class ParamsRepository(private val paramsDao: ParamsDao) {
    var cityIds: String = "28060159493"
        private set

    private lateinit var outerWithLocationMap:MutableMap<String,Any>

    fun updateOuterParam(outer:OuterParam){
        outerWithLocationMap = outer.toMap()
    }

    /**
     * 天气
     */
    fun getWeatherJson(
        outerParam: OuterParam,
        innerParam: WeatherParam,
    ): String {
//        paramsDao.insertLocationCityWeatherRequestParam(outerParam, innerParam)
        val outerMap = outerParam.toMap()
        val innerMap = innerParam.toMap() // 先转 map, 然后转 json
        outerMap["Param"] = innerMap
        return outerMap.toJsonString()
    }

    /**
     * Query City ID
     */
    fun getCityIdJson(innerParam: QueryCityIdParam): String {
        val innerMap = innerParam.toMap()
        outerWithLocationMap["Param"] = innerMap
        return outerWithLocationMap.toJsonString()
    }

    /**
     * CityList
     */
    fun getCityListByProvinceIdJson(provinceId: String): String {
        val innerMap = CityListParam(provinceId, cityIds).toMap()
        outerWithLocationMap["Param"] = innerMap
        return outerWithLocationMap.toJsonString()
    }

    /**
     * District
     */
    fun getDistrictParam(param: DistrictParam): String {
        val innerMap = param.toMap()
        outerWithLocationMap["Param"] = innerMap
        return outerWithLocationMap.toJsonString()
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Favorite
     */
    fun getFavoriteParam(cityIdList: ArrayList<String>): String {
        this.cityIds = cityIdList.joinToString(separator = ",")
        val innerParam = FavoriteParam(isauto = "1", cityids = this.cityIds)
        val innerMap = innerParam.toMap()
        outerWithLocationMap["Param"] = innerMap
        return outerWithLocationMap.toJsonString()
    }

    /**
     * 省份页面
     */
    private fun CityListParam.toMap(): MutableMap<String, Any> = mutableMapOf(
        "provId" to provId,
        "cityids" to cityids,
    )

    /**
     * 站点页面
     */
    private fun DistrictParam.toMap(): MutableMap<String, Any> = mutableMapOf(
        "cityid" to cityid,
        "obtid" to obtid,
    )

    /**
     * 首页定位城市
     */
    private fun WeatherParam.toMap(
    ) = mapOf(
        "cityid" to cityid,
        "obtid" to obtId,
        "isauto" to isauto,
        "w" to w,
        "h" to h,
        "cityids" to cityids,
        "pcity" to pcity,
        "parea" to parea,
        "lon" to lon,
        "lat" to lat,
        "gif" to gif
    )

    /**
     * 收藏页面
     */
    private fun FavoriteParam.toMap() = mapOf(
        "isauto" to isauto,
        "cityids" to cityids,
    )


    /**
     * 根据城市名字查询对应的 cityId
     */
    private fun QueryCityIdParam.toMap() = mapOf(
        "key" to key,
        "cityids" to cityids,
    )

    /**
     * 通用外部 Param
     */
    private fun OuterParam.toMap(): MutableMap<String, Any> = mutableMapOf(
        "type" to type,
        "ver" to ver,
        "rever" to rever,
        "net" to net,
        "pcity" to pcity,
        "parea" to parea,
        "lon" to lon,
        "lat" to lat,
        "gif" to gif,
        "uid" to uid,
        "uname" to uname,
        "token" to token,
        "os" to os
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

