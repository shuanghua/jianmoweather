package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.AlarmIconEntity
import dev.shuanghua.weather.data.android.database.entity.ConditionEntity
import dev.shuanghua.weather.data.android.database.entity.ExponentEntity
import dev.shuanghua.weather.data.android.database.entity.OneDayEntity
import dev.shuanghua.weather.data.android.database.entity.OneHourEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherEntity
import dev.shuanghua.weather.data.android.network.api.ShenZhenRetrofitApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather

fun ShenZhenWeather.asWeatherEntity(): WeatherEntity {

    val desc = cleanTodayDescribe()
    val airQuality = cleanAirQuality()
    val lunarCalendar = cleanCalendar()
    val (sunUp, sunDown) = cleanSunTime()

    return WeatherEntity(
        screen = "WeatherScreen",
        cityId = cityid,
        cityName = cityName,
        temperature = t,
        stationName = stationName,
        stationId = obtidyb,
        locationStationId = autoObtid,
        description = desc,
        lunarCalendar = lunarCalendar,
        sunUp = sunUp,
        sunDown = sunDown,
        airQuality = airQuality
    )
}

fun ShenZhenWeather.asAlarmEntityList(): List<AlarmIconEntity> {
    var alarmsIconUrl = ""
    return alarmList.mapIndexed { index, alarm ->
        if (alarm.icon != "") {
            alarmsIconUrl = ShenZhenRetrofitApi.ICON_HOST + alarm.icon
        }
        AlarmIconEntity(
            id = index,
            cityId = cityid,
            iconUrl = alarmsIconUrl,
            name = alarm.name
        )
    }
}

fun ShenZhenWeather.asOneHourEntityList(): List<OneHourEntity> {
    return hourForeList.mapIndexed { index, oneHour ->
        OneHourEntity(
            id = index,
            cityId = cityid,
            hour = oneHour.hour,
            t = oneHour.t,
            icon = oneHour.weatherpic,
            rain = oneHour.rain
        )
    }
}

fun ShenZhenWeather.asOneDayEntityList(): List<OneDayEntity> {
    return dayList.mapIndexed { index, oneDay ->
        OneDayEntity(
            id = index,
            cityId = cityid,
            date = oneDay.date,
            week = oneDay.week,
            desc = oneDay.desc,
            t = "${oneDay.minT}~${oneDay.maxT}",
            minT = oneDay.minT,
            maxT = oneDay.maxT,
            iconName = oneDay.wtype
        )
    }
}

//fun ShenZhenNetworkWeather.asHalfHourEntityList(): List<HalfHourEntity> {
//    return halfCircle.halfCircleList.mapIndexed { index, halfCircles ->
//        HalfHourEntity(
//            id = index,
//            cityId = cityid,
//            hour = halfCircles.hour ,
//            t = halfCircles.t
//        )
//    }
//}

fun ShenZhenWeather.asConditionEntityList(): List<ConditionEntity> {
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
                value = wf
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
fun ShenZhenWeather.asExponentEntityList(): List<ExponentEntity> {
    val healthExponents = ArrayList<ExponentEntity>()
    jkzs?.apply {
        shushidu.apply {
            val healthExponent = ExponentEntity(
                id = 0,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        gaowen.apply {
            val healthExponent = ExponentEntity(
                id = 1,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        ziwaixian.apply {
            val healthExponent = ExponentEntity(
                id = 2,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        co.apply {
            val healthExponent = ExponentEntity(
                id = 3,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        meibian.apply {
            val healthExponent = ExponentEntity(
                id = 4,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        chenlian.apply {
            val healthExponent = ExponentEntity(
                id = 5,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        luyou.apply {
            val healthExponent = ExponentEntity(
                id = 6,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        liugan.apply {
            val healthExponent = ExponentEntity(
                id = 7,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(healthExponent)
        }
        chuanyi.apply {
            val exponent = ExponentEntity(
                id = 8,
                cityId = cityid,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
            healthExponents.add(exponent)
        }
    }
    return healthExponents
}

















