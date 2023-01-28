package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.JsonClass


/**
 * NetWork ShenZhen Main Model
 * 如果有不需要的数据，删掉对应成员即可
 */
@JsonClass(generateAdapter = true)
data class ShenZhenWeather(
    val alarmList: List<Alarm> = emptyList(),
    val awords: List<String> = emptyList(),// 预警提示信息:"未来7小时内温度将升高5度"
    val cityIdList: List<CityId> = emptyList(),
    val dayList: List<Day> = emptyList(),
    val hotTopics: List<HotTopic> = emptyList(),
    val hourForeList: List<HourFore> = emptyList(),
    val rainWords: List<Any> = emptyList(),

    val halfCircle: HalfCircle,
    val halfCircleIwatch: HalfCircleIwatch?,
    val aqi: Aqi?,  // 仅支持少部分城市
    val jkzs: Jkzs?,// 仅支持深圳市
    val lunar: Lunar,
    val dialog: Dialog,


    val unexactState: Int = 0,
    val exactState: Int = 0,
    val aptmp: String = "",
    val autoObtid: String = "",
    val aword: String = "",
    val awordUrl: String = "",
    val bgweight: String = "",
    val cityName: String = "",
    val cityType: String = "",
    val cityid: String = "",
    val currTime: String = "",
    val date: String = "",
    val desc: String = "",
    val exact: String = "",
    val iday: String = "",
    val issele: String = "",
    val isshowChunYun: String = "",
    val isshowRobot: String = "",
    val maxT: String = "",
    val minT: String = "",
    val obtid: String = "",
    val obtidyb: String = "",
    val pa: String = "",
    val pageexetime: String = "",
    val pointOut: String = "",
    val pointOutTime: String = "",
    val pushSchoolSetting: String = "",
    val r01h: String = "",
    val r24h: String = "",
    val rh: String = "",
    val stationName: String = "",
    val t: String = "",
    val t_en: String = "",
    val transmit: String = "",
    val ty: String = "",
    val unexact: String = "",
    val v: String = "",
    val viewtype: String = "",
    val weatherpic: String = "",
    val week: String = "",
    val wf: String = "",
    val wg: String = "",
    val wnow: String = "",
    val wnownew: String = "",
    val wnownewIwatch: String = "",
    val ww: String = "",
    val wwCN: String = "",
)