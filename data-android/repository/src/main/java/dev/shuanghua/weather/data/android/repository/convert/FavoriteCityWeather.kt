package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.network.api.ShenZhenRetrofitApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

fun ShenZhenFavoriteCityWeather.asExternalModel() = FavoriteCityWeather(
    cityName = cityName,
    cityId = cityid,
    isAutoLocation = isauto,
    maxT = maxT,
    minT = minT,
    bgImageNew = "${ShenZhenRetrofitApi.ICON_HOST}$wnownew",
    iconUrl = "${ShenZhenRetrofitApi.ICON_HOST}$wtype",
)