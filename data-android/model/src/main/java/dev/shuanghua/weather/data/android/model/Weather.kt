package dev.shuanghua.weather.data.android.model

/**
 * external model  被
 * to entity
 * to ui model [ 用一个类包装引用即可，不需要再次转换，也就是说, 在 screen 中可以使用到该 Weather ]
 */
data class Weather(
    val cityId: String,
    val cityName: String,
    val temperature: String,
    val description: String,
    val airQuality: String,
    val airQualityIcon:String,
    val lunarCalendar: String,
    val stationName: String,
    val stationId: String,
    val locationStationId: String,
    val sunUp: String,
    val sunDown: String,

    val alarmIcons: List<AlarmIcon>,
    val oneDays: List<OneDay>,
    val oneHours: List<OneHour>,
    val conditions: List<Condition>,
    val exponents: List<Exponent>
)

val emptyWeather = Weather(
    cityId = "",
    cityName = "",
    temperature = "",
    description = "",
    airQuality = "",
    airQualityIcon = "",
    lunarCalendar = "",
    stationName = "",
    stationId = "",
    locationStationId = "",
    sunUp = "",
    sunDown = "",
    alarmIcons = emptyList(),
    oneDays = emptyList(),
    oneHours = emptyList(),
    conditions = emptyList(),
    exponents = emptyList(),
)

val previewWeather = Weather(
    cityId = "28060159493",
    cityName = "深圳(定位)",
    temperature = "26°C",
    description = "多云",
    airQuality = "26·优",
    airQualityIcon = "",
    lunarCalendar = "2022年3月12日 农历二月初十 距春分还有8天",
    stationName = "香蜜湖街道",
    stationId = "",
    locationStationId = "G3501",
    sunUp = "06:36",
    sunDown = "18:32",
    alarmIcons = emptyList(),
    oneDays = previewOnDay,
    oneHours = previewOneHour,
    conditions = previewCondition,
    exponents = previewExponent,
)