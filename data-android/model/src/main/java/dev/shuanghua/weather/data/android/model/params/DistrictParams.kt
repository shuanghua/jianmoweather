package dev.shuanghua.weather.data.android.model.params

data class DistrictParams(
    val cityId: String = "",
    val obtId: String = "",
    override var lon: String = "",
    override var lat: String = ""
) : ShenZhenParams()