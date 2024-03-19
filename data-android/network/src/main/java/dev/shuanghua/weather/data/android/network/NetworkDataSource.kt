package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.ProvinceCityParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.model.params.toMapParams
import dev.shuanghua.weather.data.android.network.api.ShenZhenApiService
import dev.shuanghua.weather.data.android.network.model.DistrictStationModel
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
import dev.shuanghua.weather.data.android.network.model.ProvinceCityModel
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather

interface NetworkDataSource {
	suspend fun getMainWeather(
		params: WeatherParams,
	): MainWeatherModel


	suspend fun getStationList(
		params: DistrictParams,
	): List<DistrictStationModel>?


	suspend fun getFavoriteCityWeather(
		params: FavoriteCityParams,
	): List<ShenZhenFavoriteCityWeather>


	suspend fun getProvinceCityList(
		params: ProvinceCityParams,
	): ProvinceCityModel

}


class NetworkDataSourceImpl(
	private val szApi: ShenZhenApiService,
) : NetworkDataSource {

	/**
	 * 首页天气 + 收藏页-站点天气
	 */
	override suspend fun getMainWeather(
		params: WeatherParams,
	): MainWeatherModel = szApi.getMainWeather(
		params.toMapParams()
	).data


	/**
	 * 观测区县 + 每个区下对应的站点列表
	 * 服务器上，非广东城市的站点列表数据为 null
	 */
	override suspend fun getStationList(
		params: DistrictParams,
	): List<DistrictStationModel>? = szApi.getStationList(
		params.toMapParams()
	).data


	/**
	 * 收藏-城市天气
	 * 收藏页面有两个请求接口
	 * 站点天气请求和首页是公用的  getMainWeather(params: WeatherParams)
	 */
	override suspend fun getFavoriteCityWeather(
		params: FavoriteCityParams,
	): List<ShenZhenFavoriteCityWeather> = szApi.getFavoriteCityWeather(
		params.toMapParams()
	).data.list


	override suspend fun getProvinceCityList(
		params: ProvinceCityParams,
	): ProvinceCityModel = szApi.getCityList(
		params.uid
	).data

}
