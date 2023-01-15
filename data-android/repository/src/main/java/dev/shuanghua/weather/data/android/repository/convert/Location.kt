package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.datastore.location.DataStoreLocation
import dev.shuanghua.weather.data.android.location.model.NetworkLocation
import dev.shuanghua.weather.data.android.model.Location

fun NetworkLocation.asExternalModel() = Location(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)


fun DataStoreLocation.asExternalModel() = Location(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)

fun Location.asDataStoreModel() = DataStoreLocation(
    cityName = cityName,
    latitude = latitude,
    longitude = longitude,
    district = district,
    address = address
)