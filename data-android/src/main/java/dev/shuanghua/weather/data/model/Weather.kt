package dev.shuanghua.weather.data.model

data class Weather(
    val screen: String,
    val cityId: String,
    val cityName: String,
    val temperature: String,
    val description: String,
    val airQuality: String,
    val lunarCalendar: String,
    val stationName: String,
    val stationId: String,
    val locationStationId: String,
    val sunUp: String,
    val sunDown: String,
)