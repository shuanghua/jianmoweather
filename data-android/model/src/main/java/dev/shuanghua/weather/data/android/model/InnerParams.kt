package dev.shuanghua.weather.data.android.model

/**
 * 用来 中转 或者 toEntity 保存还原
 */
data class InnerParams(
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
    val gif: String = Config.gif,
)


fun InnerParams.asOuterParams() = OuterParams(
    pcity = pcity,
    parea = parea,
    lon = lon,
    lat = lat,
)


/**
 * 主页的天气请求参数（站点切换也使用该参数）
 */
fun InnerParams.asMainWeatherParam() = MainWeatherParams(
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    w = w,
    h = h,
    pcity = pcity,
    parea = parea,
    gif = gif
)

fun InnerParams.asFavoriteStationWeatherParams() = FavoriteStationWeatherParams(
    lon = lon,
    lat = lat,
    isauto = isauto,
    cityids = cityids,
    cityid = cityid,
    obtId = obtId,
    w = w,
    h = h,
    pcity = pcity,
    parea = parea,
    gif = gif
)

fun InnerParams.asFavoriteCityWeatherParams() = FavoriteCityWeatherParams(
    isauto = isauto,
    cityids = cityids,
    lon = lon,
    lat = lat,
)



