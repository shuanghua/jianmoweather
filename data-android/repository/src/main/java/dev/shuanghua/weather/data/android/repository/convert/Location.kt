package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.datastore.location.OfflineLocation
import dev.shuanghua.weather.data.android.model.Location

fun Location.asDataStoreLocation() = OfflineLocation(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)

fun OfflineLocation.asExternalModel() = Location(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)