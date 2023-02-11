package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.SearchCityByKeywordsParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.ShenZhenCity
import dev.shuanghua.weather.data.android.network.model.ShenZhenDistrict
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import dev.shuanghua.weather.data.android.network.model.ShenZhenProvince
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import dev.shuanghua.weather.data.android.serializer.NetworkParamsSerialization
import javax.inject.Inject

interface NetworkDataSource {
	suspend fun getMainWeather(
		params: WeatherParams,
	): ShenZhenWeather

	suspend fun getDistrictWithStationList(
		params: DistrictParams,
	): List<ShenZhenDistrict>?

	suspend fun getFavoriteCityWeatherList(
		params: FavoriteCityParams,
	): List<ShenZhenFavoriteCityWeather>

	suspend fun getProvinceList(): List<ShenZhenProvince>

	suspend fun getCityList(
		params: CityListParams,
	): List<ShenZhenCity>

	suspend fun searchCityByKeyword(
		params: SearchCityByKeywordsParams,
	): List<ShenZhenCity>
}

class RetrofitNetworkDataSource @Inject constructor(
	private val szApi: ShenZhenApi,
	private val serializer: NetworkParamsSerialization,
) : NetworkDataSource {

	/**
	 * 首页天气 + 收藏页-站点天气
	 */
	override suspend fun getMainWeather(
		params: WeatherParams,
	): ShenZhenWeather = szApi.getMainWeather(
		serializer.weatherParamsToJson(params)
	).data

	/**
	 * 观测区县 + 每个区下对应的站点列表
	 * 服务器上，非广东城市的站点列表数据为 null
	 */
	override suspend fun getDistrictWithStationList(
		params: DistrictParams,
	): List<ShenZhenDistrict>? = szApi.getDistrictWithStationList(
		serializer.districtListParamsToJson(params)
	).data.list

	/**
	 * 收藏-城市天气
	 * 收藏页面有两个请求接口
	 * 站点天气请求和首页是公用的  getMainWeather(params: WeatherParams)
	 */
	override suspend fun getFavoriteCityWeatherList(
		params: FavoriteCityParams,
	): List<ShenZhenFavoriteCityWeather> = szApi.getFavoriteCityWeather(
		serializer.favoriteCityParamsToJson(params)
	).data.list

	/**
	 * 省份页面不需要额外参数
	 * 它只有一个 Url
	 */
	override suspend fun getProvinceList(
	): List<ShenZhenProvince> = szApi.getProvinces().data.list

	override suspend fun getCityList(
		params: CityListParams,
	): List<ShenZhenCity> = szApi.getCityList(
		serializer.cityListParamsToJson(params)
	).data.cityList

	override suspend fun searchCityByKeyword(
		params: SearchCityByKeywordsParams,
	): List<ShenZhenCity> = emptyList()

}
