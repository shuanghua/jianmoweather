package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.network.api.ShenZhenRetrofitApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather

internal fun ShenZhenWeather.cleanSunTime(): Pair<String, String> {
    var sunUp = halfCircle.sunup
    var sunDown = halfCircle.sundown
    sunUp.let {
        if (it[0] != '0') {//10点到晚上11：59
            sunUp = sunDown.also { sunDown = sunUp }
        }
    }
    return Pair(sunUp, sunDown)
}

internal fun ShenZhenWeather.cleanCalendar(): String {
    return lunar.run { "$info1 $info2 $info3 $info4 $info5" }
}

internal fun ShenZhenWeather.cleanAirQuality(): String {
    return if (aqi != null) aqi!!.aqi + "·" + aqi!!.aqic else ""
}

internal fun ShenZhenWeather.cleanAirQualityIcon(): String {
    return if (aqi != null) "${ShenZhenRetrofitApi.AQI_ICON_HOST}${aqi!!.icon}" else ""
}

internal fun ShenZhenWeather.cleanTodayDescribe(): String {
    return dayList[0].desc1
}