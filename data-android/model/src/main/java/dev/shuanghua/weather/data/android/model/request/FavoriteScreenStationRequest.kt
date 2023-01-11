package dev.shuanghua.weather.data.android.model.request

import dev.shuanghua.weather.data.android.model.FavoriteCityWeatherParams
import dev.shuanghua.weather.data.android.model.OuterParams

data class FavoriteScreenStationRequest(
    val innerParams: FavoriteCityWeatherParams,
    val outerParams: OuterParams
)
