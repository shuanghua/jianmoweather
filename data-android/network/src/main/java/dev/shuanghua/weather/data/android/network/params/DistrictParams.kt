package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.DistrictParams

/**
 * 站点页面
 */
fun DistrictParams.toMap(): MutableMap<String, Any> = mutableMapOf(
    "cityid" to cityid,
    "obtid" to obtid,
    "lon" to lon,
    "lat" to lat
)