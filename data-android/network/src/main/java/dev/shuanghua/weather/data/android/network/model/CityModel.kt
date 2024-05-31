package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 网络模型
 * 城市列表数据由于不存到数据库，所以可以直接转换成 ui model
 */
@Serializable
data class ProvinceCityModel(
	@SerialName("hotCityList") val hotCityList: List<CityModel>, // 热门城市
	@SerialName("provinceCityList") val provinceList: List<ProvinceModel> // 多个省份
)


/**
 * 省份模型
 * 一个省份下多个城市
 */
@Serializable
data class ProvinceModel(
	@SerialName("provName") val provName: String,
	@SerialName("list") val cityList: List<CityModel>
)


@Serializable
data class CityModel(
	@SerialName("cityid") val cityId: String,
	@SerialName("cityName") val cityName: String
)
