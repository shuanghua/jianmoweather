package dev.shuanghua.weather.data.network

data class WeatherParam(
    val lon: String = "",
    val lat: String = "",
    val isauto: String = CommonParam.isLocation,
    val cityids: String = CommonParam.cityids,
    val cityid: String = "",
    val obtId: String = "",
    val w: String = CommonParam.width,
    val h: String = CommonParam.height,
//    @PrimaryKey
    val pcity: String = "",
    val parea: String = "",
    val gif: String = CommonParam.gif
) {
    companion object {
        // 只有首次安装APP 并且 定位失败 才会使用 APP 设定的默认参数
        val DEFAULT = WeatherParam()
    }
}
