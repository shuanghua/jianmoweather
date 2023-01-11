package dev.shuanghua.weather.data.android.model.request

import dev.shuanghua.weather.data.android.model.CityListParams
import dev.shuanghua.weather.data.android.model.OuterParams

data class CityScreenRequest(
    val innerParams: CityListParams,
    val outerParams: OuterParams
)
