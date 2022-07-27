package dev.shuanghua.weather.data.network

data class DistrictParam(
    val cityid: String = "",
    val obtid: String = "",
) {
    companion object {
        val DEFAULT = DistrictParam()
    }
}