package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * NetWork ShenZhen Main Model
 * 如果有不需要的数据，删掉对应成员即可
 */
@JsonClass(generateAdapter = true)
data class ShenZhenWeather(
    @field:Json(name = "alarmList") val alarmList: List<Alarm> = emptyList(),
    @field:Json(name = "awords") val awords: List<String> = emptyList(),// 预警提示信息:"未来7小时内温度将升高5度"
    @field:Json(name = "cityIdList") val cityIdList: List<CityId> = emptyList(),
    @field:Json(name = "dayList") val dayList: List<Day> = emptyList(),
    @field:Json(name = "hourForeList") val hourForeList: List<HourFore> = emptyList(),
    @field:Json(name = "rainWords") val rainWords: List<Any> = emptyList(),

    @field:Json(name = "halfCircle") val halfCircle: HalfCircle,
    @field:Json(name = "halfCircleIwatch") val halfCircleIwatch: HalfCircleIwatch?,
    @field:Json(name = "aqi") val aqi: Aqi?,  // 仅支持少部分城市
    @field:Json(name = "jkzs") val jkzs: Jkzs?,// 仅支持深圳市
    @field:Json(name = "lunar") val lunar: Lunar,

    @field:Json(name = "autoObtid") val autoObtid: String = "",
    @field:Json(name = "aword") val aword: String = "",
    @field:Json(name = "awordUrl") val awordUrl: String = "",
    @field:Json(name = "cityName") val cityName: String = "",
    @field:Json(name = "cityType") val cityType: String = "",
    @field:Json(name = "cityid") val cityid: String = "",
    @field:Json(name = "currTime") val currTime: String = "",
    @field:Json(name = "date") val date: String = "",
    @field:Json(name = "desc") val desc: String = "",
    @field:Json(name = "maxT") val maxT: String = "",
    @field:Json(name = "minT") val minT: String = "",
    @field:Json(name = "obtid") val obtid: String = "",
    @field:Json(name = "obtidyb") val obtidyb: String = "",
    @field:Json(name = "pa") val pa: String = "",
    @field:Json(name = "pointOut") val pointOut: String = "",
    @field:Json(name = "pointOutTime") val pointOutTime: String = "",
    @field:Json(name = "r01h") val r01h: String = "",
    @field:Json(name = "r24h") val r24h: String = "",
    @field:Json(name = "v") val v: String = "",
    @field:Json(name = "rh") val rh: String = "",
    @field:Json(name = "stationName") val stationName: String = "",
    @field:Json(name = "t") val t: String = "",
    @field:Json(name = "week") val week: String = "",
    @field:Json(name = "wf") val wf: String = "",
    @field:Json(name = "wg") val wg: String = "",
    @field:Json(name = "wnow") val wnow: String = "",
    @field:Json(name = "wnownew") val wnownew: String = "",
    @field:Json(name = "wnownewIwatch") val wnownewIwatch: String = "",
    @field:Json(name = "ww") val ww: String = "",
    @field:Json(name = "wwCN") val wwCN: String = "",
)

@JsonClass(generateAdapter = true)
data class ShenZhenWeather2(
    @field:Json(name = "autoObtid") val autoObtid: String = "",
    @field:Json(name = "aword") val aword: String = "",
    @field:Json(name = "awordUrl") val awordUrl: String = "",
    @field:Json(name = "cityName") val cityName: String = "",
)