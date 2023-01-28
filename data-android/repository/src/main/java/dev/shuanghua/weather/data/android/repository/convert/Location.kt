package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.location.model.NetworkLocation
import dev.shuanghua.weather.data.android.model.Location

fun NetworkLocation.asExternalModel() = Location(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)