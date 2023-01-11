package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.CityListParams

fun CityListParams.toMap(): MutableMap<String, Any> = mutableMapOf(
    "provId" to provId,
    "cityids" to cityids,
)