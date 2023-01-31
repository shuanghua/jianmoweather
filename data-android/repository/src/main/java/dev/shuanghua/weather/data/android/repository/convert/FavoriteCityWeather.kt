package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

fun ShenZhenFavoriteCityWeather.asExternalModel() = FavoriteCity(
    cityName = cityName,
    cityId = cityid,
    isAutoLocation = isauto,
    maxT = maxT,
    minT = minT,
    bgImageNew = "${ShenZhenApi.IMAGE_URL}$wnownew",
    iconUrl = "${ShenZhenApi.IMAGE_URL}$wtype"
)