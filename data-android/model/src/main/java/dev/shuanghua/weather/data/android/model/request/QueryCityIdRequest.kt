package dev.shuanghua.weather.data.android.model.request

import dev.shuanghua.weather.data.android.model.OuterParams
import dev.shuanghua.weather.data.android.model.QueryCityIdParams

data class QueryCityIdRequest(
    val innerParams: QueryCityIdParams,
    val outerParams: OuterParams
)
