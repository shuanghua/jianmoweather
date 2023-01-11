package dev.shuanghua.weather.data.android.model

data class FavoriteCityWeather(
    val cityName: String,
    val cityId: String,
    val isAutoLocation: String,
    val maxT: String,
    val minT: String,
    val bgImageNew: String,
    val iconUrl: String
)
