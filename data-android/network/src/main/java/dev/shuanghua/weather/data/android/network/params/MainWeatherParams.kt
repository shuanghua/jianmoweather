package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.MainWeatherParams


/**
 * 首页定位城市
 */
internal fun MainWeatherParams.toMap() = mapOf(
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