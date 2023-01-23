package dev.shuanghua.weather.data.android.model.params

data class WeatherParams(
    val isAuto: String = "",
    val cityIds: String = "",
    val cityId: String = "",
    val obtId: String = "",
    override var cityName: String = "",
    override var district: String = "",
    override var lon: String = "",
    override var lat: String = ""
) : ShenZhenParams() {
    override fun toString(): String {
        return "WeatherParams:" +
                "{isAuto:$isAuto, " +
                "cityIds:$cityIds, " +
                "cityId:$cityId, " +
                "obtId:$obtId, " +
                "cityName:$cityName, " +
                "district:$district, " +
                "lon:$lon, " +
                "lat:$lat, " +
                "gif:$gif, " +
                "w:$w, " +
                "h:$h}"
    }
}