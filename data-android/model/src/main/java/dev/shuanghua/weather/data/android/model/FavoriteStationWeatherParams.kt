package dev.shuanghua.weather.data.android.model

data class FavoriteStationWeatherParams(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = Config.isLocation,
    val cityids: String = Config.cityids,
    val cityid: String = "",
    val obtId: String = "",
    val w: String = Config.width,
    val h: String = Config.height,
    val pcity: String = "",
    val parea: String = "",
    val gif: String = Config.gif
)
