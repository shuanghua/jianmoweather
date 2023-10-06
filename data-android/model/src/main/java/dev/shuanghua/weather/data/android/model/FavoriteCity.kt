package dev.shuanghua.weather.data.android.model

data class FavoriteCity(
    val cityName: String,
    val cityId: String,
    val isAutoLocation: String = "",
    val currentT: String,
    val bgImageNew: String,
    val iconUrl: String
)
