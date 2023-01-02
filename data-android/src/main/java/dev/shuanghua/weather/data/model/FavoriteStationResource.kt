package dev.shuanghua.weather.data.model

data class FavoriteStationResource(
    val stationName: String,
    val cityName: String = "",
    val parea: String = ""
)
