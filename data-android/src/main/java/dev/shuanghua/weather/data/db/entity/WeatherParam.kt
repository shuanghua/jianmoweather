package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "param_weather")
data class WeatherParam(
    val lon: String = "", //
    val lat: String = "", //
    val isauto: String = OuterParam.ISLOCATION,
    val cityids: String = OuterParam.CITYIDS,
    val cityid: String = "", //
    val w: String = OuterParam.WIDTH,
    val h: String = OuterParam.HEIGHT,
    @PrimaryKey
    val pcity: String = "",
    val parea: String = "",
    val gif: String = OuterParam.gif
) {
    companion object {
        // 只有首次安装APP 并且 定位失败 才会使用 APP 设定的默认参数
        val DEFAULT = WeatherParam()
    }
}