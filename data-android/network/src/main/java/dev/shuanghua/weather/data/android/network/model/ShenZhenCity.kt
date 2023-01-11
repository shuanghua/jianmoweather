package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shuanghua.weather.data.android.model.City

@JsonClass(generateAdapter = true)
data class CityReturn(@Json(name = "list") val cityList: List<ShenZhenCity>?)

/**
 * 用于深圳Api的网络数据模型
 * 城市列表数据由于不存到数据库，所以可以直接转换成 ui model
 */
@JsonClass(generateAdapter = true)
data class ShenZhenCity(
    @Json(name = "cityName") val name: String,
    @Json(name = "cityid") val id: String,
)
