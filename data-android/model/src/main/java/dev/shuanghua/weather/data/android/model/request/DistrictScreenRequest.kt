package dev.shuanghua.weather.data.android.model.request

import dev.shuanghua.weather.data.android.model.DistrictParams
import dev.shuanghua.weather.data.android.model.OuterParams

data class DistrictScreenRequest(
   val innerParams: DistrictParams,
   val outerParams: OuterParams
)
