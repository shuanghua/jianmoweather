package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.SzwModel

internal fun SzwModel.cleanSunTime(): Pair<String, String> {
    var sunUp = halfCircle.sunup
    var sunDown = halfCircle.sundown
    sunUp.let {
        if (it[0] != '0') {//10点到晚上11：59
            sunUp = sunDown.also { sunDown = sunUp }
        }
    }
    return Pair(sunUp, sunDown)
}

internal fun SzwModel.cleanCalendar(): String {
    return lunar.run { "$info1 $info2 $info3 $info4 $info5" }
}

internal fun SzwModel.cleanAirQuality(): String {
    return if (aqi != null) aqi!!.aqi + "·" + aqi!!.aqic else ""
}

internal fun SzwModel.cleanAirQualityIcon(): String {
    return if (aqi != null) {
        "${ShenZhenApi.AQI_IMAGE_URL}${aqi!!.icon.replace("iconOther/", "")}"
    } else ""
}

//internal fun SzwModel.cleanTodayDescribe(): String {
//    return dayList[0].desc1
//}