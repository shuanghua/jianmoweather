package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 网络模型
 * 城市列表数据由于不存到数据库，所以可以直接转换成 ui model
 */
@JsonClass(generateAdapter = true)
data class ProvinceCityModel(
	@field:Json(name = "hotCityList") val hotCityList: List<CityModel>, // 热门城市
	@field:Json(name = "provinceCityList") val provinceList: List<ProvinceModel> // 多个省份
)


/**
 * 省份模型
 * 一个省份下多个城市
 */
@JsonClass(generateAdapter = true)
data class ProvinceModel(
	@field:Json(name = "provName") val provName: String,
	@field:Json(name = "list") val cityList: List<CityModel>
)


@JsonClass(generateAdapter = true)
data class CityModel(
	@field:Json(name = "cityid") val cityId: String,
	@field:Json(name = "cityName") val cityName: String
)
