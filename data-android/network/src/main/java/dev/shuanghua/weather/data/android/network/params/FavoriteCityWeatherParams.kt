package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.FavoriteCityWeatherParams

/**
 * 收藏页面
 */
fun FavoriteCityWeatherParams.toMap() = mapOf(
    "isauto" to isauto,
    "cityids" to cityids,
)