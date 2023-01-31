package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityReturn(@field:Json(name = "list") val cityList: List<ShenZhenCity>)

/**
 * 用于深圳Api的网络数据模型
 * 城市列表数据由于不存到数据库，所以可以直接转换成 ui model
 */
@JsonClass(generateAdapter = true)
data class ShenZhenCity(
    @field:Json(name = "cityName") val name: String,
    @field:Json(name = "cityid") val id: String,
)
