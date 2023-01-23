package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.SearchCityByKeywordsParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams

fun WeatherParams.toMapParams(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to version,
    "rever" to rever,
    "net" to net,
    "pcity" to cityName,
    "parea" to district,
    "lon" to lon,
    "lat" to lat,
    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os,
    "Param" to mutableMapOf<String, Any>(
        "cityid" to cityId,
        "obtid" to obtId,
        "isauto" to isAuto,
        "w" to w,
        "h" to h,
        "cityids" to cityIds,
        "pcity" to cityName,
        "parea" to district,
        "lon" to lon,
        "lat" to lat,
        "gif" to gif
    )
)

fun DistrictParams.toMapParams(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to version,
    "rever" to rever,
    "net" to net,
    "pcity" to cityName,
    "parea" to district,
    "lon" to lon,
    "lat" to lat,
    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os,
    "Param" to mutableMapOf(
        "cityid" to cityId,
        "obtid" to obtId,
        "lon" to lon,
        "lat" to lat
    )
)

fun FavoriteCityParams.toMapParams(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to version,
    "rever" to rever,
    "net" to net,

    "pcity" to cityName,
    "parea" to district,
    "lon" to lon,
    "lat" to lat,

    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os,
    "Param" to mutableMapOf(
        "isauto" to isAuto,
        "cityids" to cityIds,
    )
)

//--------------------------------------------------------------------------------------------------

/**
 * 根据省份ID查询城市列表（省份列表只要一个完整的 URL即可， 无需其它参数）
 */
fun CityListParams.toMapParams(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to version,
    "rever" to rever,
    "net" to net,
    "pcity" to cityName,
    "parea" to district,
    "lon" to lon,
    "lat" to lat,
    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os,
    "Param" to mutableMapOf(
        "provId" to provId,
        "cityids" to cityIds,
    )
)

fun SearchCityByKeywordsParams.toMapParams(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to version,
    "rever" to rever,
    "net" to net,
    "pcity" to cityName,
    "parea" to district,
    "lon" to lon,
    "lat" to lat,
    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os,
    "Param" to mutableMapOf(
        "key" to keywords,
        "cityids" to cityIds,
    )
)


