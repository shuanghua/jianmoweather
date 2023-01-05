package dev.shuanghua.weather.data.model

import com.squareup.moshi.JsonClass
import dev.shuanghua.weather.data.db.entity.AlarmIconEntity
import dev.shuanghua.weather.data.db.entity.AutoLocationStation
import dev.shuanghua.weather.data.db.entity.ConditionEntity
import dev.shuanghua.weather.data.db.entity.ExponentEntity
import dev.shuanghua.weather.data.db.entity.OneDayEntity
import dev.shuanghua.weather.data.db.entity.OneHourEntity
import dev.shuanghua.weather.data.db.entity.WeatherEntity
import dev.shuanghua.weather.data.network.ShenZhenWeatherApi
import dev.shuanghua.weather.shared.extensions.ifNullToEmpty
import dev.shuanghua.weather.shared.extensions.ifNullToValue

/**
 * NetWork ShenZhen Main Model
 * 如果有不需要的数据，删掉对应成员即可
 */
@JsonClass(generateAdapter = true)
data class ShenZhenNetworkWeather(
    val alarmList: List<Alarm> = emptyList(),
    val awords: List<String> = emptyList(),// 预警提示信息:"未来7小时内温度将升高5度"
    val cityIdList: List<CityId> = emptyList(),
    val dayList: List<Day> = emptyList(),
    val hotTopics: List<HotTopic> = emptyList(),
    val hourForeList: List<HourFore> = emptyList(),
    val rainWords: List<Any> = emptyList(),

    val halfCircle: HalfCircle,
    val halfCircleIwatch: HalfCircleIwatch,
    val aqi: Aqi?,  // 仅支持少部分城市
    val jkzs: Jkzs?,// 仅支持深圳市
    val lunar: Lunar,

    val unexactState: Int = 0,
    val exactState: Int = 0,

    val aptmp: String = "",
    val autoObtid: String = "",
    val dialog: Dialog,
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

@JsonClass(generateAdapter = true)
data class Alarm(
    val icon: String = "",
    val name: String = "",
)

@JsonClass(generateAdapter = true)
data class CityId(
    val cityid: String = "",
    val isauto: String = "",
)

@JsonClass(generateAdapter = true)
data class Day(
    val date: String = "",
    val desc: String = "",
    val desc1: String = "",
    val desc_en: String = "",
    val maxT: String = "",
    val minT: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val week: String = "",
    val week_en: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)

@JsonClass(generateAdapter = true)
data class Dialog(
    val messageList: List<Message> = emptyList(),
    val robotIcon4And: String = "",
    val robotIcon4Ios: String = "",
)

@JsonClass(generateAdapter = true)
data class HalfCircle(
    val downicon: String,
    val halfCircleList: List<HalfCircleX> = emptyList(),
    val size: Int = 0,
    val sundown: String = "",
    val sunup: String = "",
    val upicon: String = "",
)

@JsonClass(generateAdapter = true)
data class HalfCircleIwatch(
    val downicon: String = "",
    val halfCircleList: List<HalfCircleXX> = emptyList(),
    val size: Int = 0,
    val sundown: String = "",
    val sunup: String = "",
    val upicon: String = "",
)

@JsonClass(generateAdapter = true)
data class HotTopic(
    val color: String = "",
    val hpImbtime: String = "",
    val hpImetime: String = "",
    val issele: String = "",
    val wnid: String = "",
    val wnname: String = "",
    val wnurl: String = "",
)

@JsonClass(generateAdapter = true)
data class HourFore(
    val desc_en: String = "",
    val hour: String = "",
    val hour_en: String = "",
    val rain: String = "",
    val t: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val weatherstatus: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)

@JsonClass(generateAdapter = true)
data class Jkzs(
    val chenlian: Chenlian,
    val chuanyi: Chuanyi,
    val co: Co,
    val gaowen: Gaowen,
    val liugan: Liugan,
    val luyou: Luyou,
    val meibian: Meibian,
    val shushidu: Shushidu,
    val ziwaixian: Ziwaixian,
)

@JsonClass(generateAdapter = true)
data class Lunar(
    val info1: String = "",
    val info2: String = "",
    val info3: String = "",
    val info4: String = "",
    val info5: String = "",
)

//@JsonClass(generateAdapter = true)
//class Notable

@JsonClass(generateAdapter = true)
data class Message(
    val tip: String = "",
)

@JsonClass(generateAdapter = true)
data class HalfCircleX(
    val desc_en: String = "",
    val hour: String = "",
    val hour_en: String = "",
    val num: Int = 0,
    val rain: String = "",
    val t: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val weatherstatus: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)

@JsonClass(generateAdapter = true)
data class HalfCircleXX(
    val desc_en: String = "",
    val hour: String = "",
    val hour_en: String = "",
    val num: Int = 0,
    val rain: String = "",
    val t: String = "",
    val wd: String = "",
    val wd_en: String = "",
    val weatherpic: String = "",
    val weatherstatus: String = "",
    val wf: String = "",
    val wf_en: String = "",
    val wtype: String = "",
)

@JsonClass(generateAdapter = true)
data class Chenlian(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Chuanyi(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Co(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Gaowen(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Liugan(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Luyou(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Meibian(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Shushidu(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Ziwaixian(
    val level: String = "",
    val level_advice: String = "",
    val level_desc: String = "",
    val title: String = "",
)

@JsonClass(generateAdapter = true)
data class Aqi(
    val aqi: String = "", // 24
    val aqic: String = "", // 优
    val icon: String = "",
)


fun ShenZhenNetworkWeather.asWeatherEntity(): WeatherEntity {
    var sunUp = halfCircle.sunup
    var sunDown = halfCircle.sundown
    sunUp.let {
        if (it[0] != '0') {//10点到晚上11：59
            sunUp = sunDown.also { sunDown = sunUp }
        }
    }
    val lunarCalendar: String = lunar.run { "$info1 $info2 $info3 $info4 $info5" }
    val airQuality = if (aqi != null)
        aqi.aqi.ifNullToValue() + "·" + aqi.aqic.ifNullToValue() else ""

    val desc = dayList[0].desc1
    return WeatherEntity(
        screen = "WeatherScreen",
        cityId = cityid.ifNullToEmpty(),
        cityName = cityName.ifNullToEmpty(),
        temperature = t.ifNullToEmpty(),
        stationName = stationName.ifNullToEmpty(),
        stationId = obtidyb.ifNullToEmpty(),
        locationStationId = autoObtid.ifNullToEmpty(),
        description = desc.ifNullToEmpty(),
        lunarCalendar = lunarCalendar.ifNullToEmpty(),
        sunUp = sunUp.ifNullToEmpty(),
        sunDown = sunDown.ifNullToEmpty(),
        airQuality = airQuality
    )
}

fun ShenZhenNetworkWeather.asAlarmEntityList(): List<AlarmIconEntity> {
    var alarmsIconUrl = ""
    return alarmList.mapIndexed { index, alarm ->
        if (alarm.icon != "") {
            alarmsIconUrl = ShenZhenWeatherApi.ICON_HOST + alarm.icon
        }
        AlarmIconEntity(
            id = index,
            cityId = cityid,
            iconUrl = alarmsIconUrl,
            name = alarm.name.ifNullToValue()
        )
    }
}

fun ShenZhenNetworkWeather.asOneHourEntityList(): List<OneHourEntity> {
    return hourForeList.mapIndexed { index, oneHour ->
        OneHourEntity(
            id = index,
            cityId = cityid,
            hour = oneHour.hour.ifNullToValue(),
            t = oneHour.t.ifNullToValue(),
            icon = oneHour.weatherpic.ifNullToValue(),
            rain = oneHour.rain.ifNullToValue()
        )
    }
}

fun ShenZhenNetworkWeather.asOneDayEntityList(): List<OneDayEntity> {
    return dayList.mapIndexed { index, oneDay ->
        OneDayEntity(
            id = index,
            cityId = cityid,
            date = oneDay.date.ifNullToValue(),
            week = oneDay.week.ifNullToValue(),
            desc = oneDay.desc.ifNullToValue(),
            t = "${oneDay.minT}~${oneDay.maxT}",
            minT = oneDay.minT.ifNullToValue(),
            maxT = oneDay.maxT.ifNullToValue(),
            iconName = oneDay.wtype.ifNullToValue()
        )
    }
}

//fun ShenZhenNetworkWeather.asHalfHourEntityList(): List<HalfHourEntity> {
//    return halfCircle.halfCircleList.mapIndexed { index, halfCircles ->
//        HalfHourEntity(
//            id = index,
//            cityId = cityid,
//            hour = halfCircles.hour.ifNullToValue(),
//            t = halfCircles.t.ifNullToValue()
//        )
//    }
//}

fun ShenZhenNetworkWeather.mapToConditionEntityList(): List<ConditionEntity> {
    val conditions = ArrayList<ConditionEntity>()
    if (rh.isNotEmpty()) {
        val rhItem = ConditionEntity(
            id = 0,
            cityId = cityid,
            name = "湿度",
            value = rh
        )
        conditions.add(rhItem)
    }
    if (pa.isNotEmpty()) {
        val hPaItem = ConditionEntity(
            id = 1,
            cityId = cityid,
            name = "气压",
            value = pa
        )
        conditions.add(hPaItem)
    }
    if (wwCN.isNotEmpty()) {//东风
        val windItem =
            ConditionEntity(
                id = 2,
                cityId = cityid,
                name = wwCN,
                value = wf.ifNullToValue()
            )
        conditions.add(windItem)
    }
    if (r24h.isNotEmpty()) {
        val r24hItem = ConditionEntity(
            id = 3,
            cityId = cityid,
            name = "24H降雨量",
            value = r24h
        )
        conditions.add(r24hItem)
    }
    if (r01h.isNotEmpty()) {
        val r01hItem = ConditionEntity(
            id = 4,
            cityId = cityid,
            name = "1H降雨量",
            value = r01h
        )
        conditions.add(r01hItem)
    }
    if (v.isNotEmpty()) {
        val visibilityItem = ConditionEntity(
            id = 5,
            cityId = cityid,
            name = "能见度",
            value = v
        )
        conditions.add(visibilityItem)
    }
    return conditions
}


/**
 * 健康指数仅支持深圳地区
 */
fun ShenZhenNetworkWeather.mapToExponentEntityList(): List<ExponentEntity> {
    val healthExponents = ArrayList<ExponentEntity>()
    jkzs?.apply {
        shushidu.apply {
            val healthExponent = ExponentEntity(
                id = 0,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        gaowen.apply {
            val healthExponent = ExponentEntity(
                id = 1,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        ziwaixian.apply {
            val healthExponent = ExponentEntity(
                id = 2,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        co.apply {
            val healthExponent = ExponentEntity(
                id = 3,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        meibian.apply {
            val healthExponent = ExponentEntity(
                id = 4,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        chenlian.apply {
            val healthExponent = ExponentEntity(
                id = 5,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        luyou.apply {
            val healthExponent = ExponentEntity(
                id = 6,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        liugan.apply {
            val healthExponent = ExponentEntity(
                id = 7,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(healthExponent)
        }
        chuanyi.apply {
            val exponent = ExponentEntity(
                id = 8,
                cityId = cityid,
                title = title.ifNullToValue(),
                level = level.ifNullToValue(),
                levelDesc = level_desc.ifNullToValue(),
                levelAdvice = level_advice.ifNullToValue()
            )
            healthExponents.add(exponent)
        }
    }
    return healthExponents
}

fun ShenZhenNetworkWeather.mapToAutoLocationStationEntity(): AutoLocationStation {
    return if (autoObtid != "") {
        AutoLocationStation(
            screen = "StationScreen",
            id = autoObtid,
            name = stationName.ifNullToEmpty()
        )
    } else {
        AutoLocationStation(
            screen = "StationScreen",
            id = "",
            name = ""
        )
    }
}