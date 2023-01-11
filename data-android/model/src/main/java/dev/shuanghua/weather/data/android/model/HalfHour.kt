package dev.shuanghua.weather.data.android.model

data class HalfHour(
    val id: Int,
    val cityId: String,
    val hour: String,//未来半小时时间
    val t: String,//未来半个小时天气
)
