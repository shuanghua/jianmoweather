package dev.shuanghua.weather.data.android.model.request

import dev.shuanghua.weather.data.android.model.MainWeatherParams
import dev.shuanghua.weather.data.android.model.OuterParams

data class WeatherScreenRequest(
    val innerParams: MainWeatherParams,
    val outerParams: OuterParams
)
