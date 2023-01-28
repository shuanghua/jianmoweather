package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.network.api.ShenZhenWeatherApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

fun ShenZhenFavoriteCityWeather.asExternalModel() = FavoriteCity(
    cityName = cityName,
    cityId = cityid,
    isAutoLocation = isauto,
    maxT = maxT,
    minT = minT,
    bgImageNew = "${ShenZhenWeatherApi.IMAGE_URL}$wnownew",
    iconUrl = "${ShenZhenWeatherApi.IMAGE_URL}$wtype"
)