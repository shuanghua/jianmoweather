package dev.shuanghua.weather.data.db.pojo

import dev.shuanghua.weather.data.db.entity.OuterParam
import dev.shuanghua.weather.data.db.entity.WeatherParam

data class LastRequestParams(
    val lastOuterParam: OuterParam? = null,
    val lastMainWeatherParam: WeatherParam? = null
)