package dev.shuanghua.weather.data.android.model

data class FavoriteStation(
    val cityId: String,
    val stationName: String,
    val temperature: String,
    val weatherStatus: String,
    val weatherIcon: String,
    val rangeT: String
)
