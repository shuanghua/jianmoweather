package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.QueryCityIdParams

/**
 * 根据城市名字查询对应的 cityId
 */
fun QueryCityIdParams.toMap() = mapOf(
    "key" to keyword,
    "cityids" to cityIds,
)