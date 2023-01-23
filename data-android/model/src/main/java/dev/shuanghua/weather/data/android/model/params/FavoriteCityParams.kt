package dev.shuanghua.weather.data.android.model.params

data class FavoriteCityParams(
    val isAuto: String = "0",
    val cityIds: String,
    override var lon: String = "",
    override var lat: String = ""
) : ShenZhenParams()