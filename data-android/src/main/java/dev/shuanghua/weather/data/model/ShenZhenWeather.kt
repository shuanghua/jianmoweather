package dev.shuanghua.weather.data.model

import com.squareup.moshi.JsonClass

/**
 * NetWork ShenZhen Main Model
 */
@JsonClass(generateAdapter = true)
data class ShenZhenWeather(
    val alarmList: List<Alarm>?,
    val androidMapVer: String?,
    val aptmp: String?,
    val autoObtid: String?,
    val aqi: Aqi?,
    val aword: String?,
    val awordUrl: String?,
    val awords: List<String>?,
    val bgweight: String?,
    val cityName: String?,
    val cityType: String?,
    val cityid: String?,
    val cityIdList: List<CityId>?,
    val currTime: String?,
    val date: String?,
    val dayList: List<Day>?,
    val desc: String?,
    val dialog: Dialog,
    val exact: String?,
    val exactState: Int?,
    val halfCircle: HalfCircle?,
    val halfCircleIwatch: HalfCircleIwatch?,
    val hotTopics: List<HotTopic>?,
    val hourForeList: List<HourFore>?,
    val iconList1: IconList1?,
    val iconPushsetting: IconPushsetting?,
    val iday: String?,
    val iosMapVer: String?,
    val issele: String?,
    val isshowChunYun: String?,
    val isshowRobot: String?,
    val jkzs: Jkzs?,
    val lunar: Lunar?,
    val maxT: String?,
    val minT: String?,
//    val notable: Notable?,
    val obtid: String?,
    val obtidyb: String?,
    val pa: String?,
    val pageexetime: String?,
    val pointOut: String?,
    val pointOutTime: String?,
    val pushSchoolSetting: String?,
    val r01h: String?,
    val r24h: String?,
    val rainWords: List<Any>?,
    val rh: String?,
    val stationName: String?,
    val t: String?,
    val t_en: String?,
    val transmit: String?,
    val ty: String?,
    val unexact: String?,
    val unexactState: Int?,
    val v: String?,
    val viewtype: String?,
    val weatherpic: String?,
    val week: String?,
    val wf: String?,
    val wg: String?,
    val wnow: String?,
    val wnownew: String?,
    val wnownewIwatch: String?,
    val ww: String?,
    val wwCN: String?
)

@JsonClass(generateAdapter = true)
data class Alarm(
    val icon: String?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class CityId(
    val cityid: String?,
    val isauto: String?
)

@JsonClass(generateAdapter = true)
data class Day(
    val date: String?,
    val desc: String?,
    val desc1: String?,
    val desc_en: String?,
    val maxT: String?,
    val minT: String?,
    val wd: String?,
    val wd_en: String?,
    val weatherpic: String?,
    val week: String?,
    val week_en: String?,
    val wf: String?,
    val wf_en: String?,
    val wtype: String?
)

@JsonClass(generateAdapter = true)
data class Dialog(
    val messageList: List<Message>?,
    val robotIcon4And: String?,
    val robotIcon4Ios: String?
)

@JsonClass(generateAdapter = true)
data class HalfCircle(
    val downicon: String?,
    val halfCircleList: List<HalfCircleX>?,
    val size: Int?,
    val sundown: String?,
    val sunup: String?,
    val upicon: String?
)

@JsonClass(generateAdapter = true)
data class HalfCircleIwatch(
    val downicon: String?,
    val halfCircleList: List<HalfCircleXX>?,
    val size: Int?,
    val sundown: String?,
    val sunup: String?,
    val upicon: String?
)

@JsonClass(generateAdapter = true)
data class HotTopic(
    val color: String?,
    val hpImbtime: String?,
    val hpImetime: String?,
    val issele: String?,
    val wnid: String?,
    val wnname: String?,
    val wnurl: String?
)

@JsonClass(generateAdapter = true)
data class HourFore(
    val desc_en: String?,
    val hour: String?,
    val hour_en: String?,
    val rain: String?,
    val t: String?,
    val wd: String?,
    val wd_en: String?,
    val weatherpic: String?,
    val weatherstatus: String?,
    val wf: String?,
    val wf_en: String?,
    val wtype: String?
)

@JsonClass(generateAdapter = true)
data class IconList1(
    val icon: String?,
    val name: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class IconPushsetting(
    val icon: String?,
    val name: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class Jkzs(
    val chenlian: Chenlian?,
    val chuanyi: Chuanyi?,
    val co: Co?,
    val gaowen: Gaowen?,
    val liugan: Liugan?,
    val luyou: Luyou?,
    val meibian: Meibian?,
    val shushidu: Shushidu?,
    val ziwaixian: Ziwaixian?
)

@JsonClass(generateAdapter = true)
data class Lunar(
    val info1: String?,
    val info2: String?,
    val info3: String?,
    val info4: String?,
    val info5: String?
)

//@JsonClass(generateAdapter = true)
//class Notable

@JsonClass(generateAdapter = true)
data class Message(
    val tip: String?
)

@JsonClass(generateAdapter = true)
data class HalfCircleX(
    val desc_en: String?,
    val hour: String?,
    val hour_en: String?,
    val num: Int?,
    val rain: String?,
    val t: String?,
    val wd: String?,
    val wd_en: String?,
    val weatherpic: String?,
    val weatherstatus: String?,
    val wf: String?,
    val wf_en: String?,
    val wtype: String?
)

@JsonClass(generateAdapter = true)
data class HalfCircleXX(
    val desc_en: String?,
    val hour: String?,
    val hour_en: String?,
    val num: Int?,
    val rain: String?,
    val t: String?,
    val wd: String?,
    val wd_en: String?,
    val weatherpic: String?,
    val weatherstatus: String?,
    val wf: String?,
    val wf_en: String?,
    val wtype: String?
)

@JsonClass(generateAdapter = true)
data class Chenlian(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Chuanyi(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Co(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Gaowen(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Liugan(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Luyou(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Meibian(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Shushidu(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Ziwaixian(
    val level: String?,
    val level_advice: String?,
    val level_desc: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Aqi(
    val aqi: String?, // 24
    val aqic: String?, // 优
    val icon: String?
)