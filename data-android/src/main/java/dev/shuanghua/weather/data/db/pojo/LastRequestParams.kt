package dev.shuanghua.weather.data.db.pojo

import dev.shuanghua.weather.data.network.MainWeatherParam
import dev.shuanghua.weather.data.network.OuterParam

data class LastRequestParams(
    val lastOuterParam: OuterParam? = null,
    val lastMainWeatherParam: MainWeatherParam? = null
)