package dev.shuanghua.weather.data.android.network

import android.content.Context
import dev.shuanghua.weather.data.android.model.params.ProvinceCityParams
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.api.ShenZhenApiService
import dev.shuanghua.weather.data.android.network.model.CommonResult
import dev.shuanghua.weather.data.android.network.model.DistrictStationModel
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
import dev.shuanghua.weather.data.android.network.model.ProvinceCityModel
import dev.shuanghua.weather.data.android.network.model.ShenZhenFavoriteCityWeather
import kotlinx.serialization.json.Json

interface NetworkDataSource {
	suspend fun getCityWeatherByLocation(
		latitude: String,
		longitude: String,
		cityName: String,
		district: String,
	): MainWeatherModel

	suspend fun getCityWeatherByCityId(cityId: String): MainWeatherModel

	suspend fun getStationList(
		params: Map<String, String>,
	): List<DistrictStationModel>?

	suspend fun getFavoriteCityWeather(
		params: Map<String, String>,
	): List<ShenZhenFavoriteCityWeather>

	suspend fun getCityWeatherByStationId(
		cityId: String,
		stationId: String,
	): MainWeatherModel

	suspend fun getProvinceCityList(
		params: ProvinceCityParams,
	): ProvinceCityModel

	suspend fun getProvinceCityListFromAssets(): ProvinceCityModel
}


internal class NetworkDataSourceImpl(
	private val context: Context,
	private val szApi: ShenZhenApiService,
) : NetworkDataSource {

	/**
	 * 首页天气 + 收藏页-站点天气
	 */
	override suspend fun getCityWeatherByLocation(
		latitude: String,
		longitude: String,
		cityName: String,
		district: String,
	): MainWeatherModel = szApi.getMainWeather(
		mapOf(
			"lat" to latitude,
			"lon" to longitude,
			"pcity" to cityName,
			"parea" to district,
			"rainm" to Api2.RAINM,
			"uid" to Api2.UID,
		)
	).data

	override suspend fun getCityWeatherByStationId(
		cityId: String,
		stationId: String,
	): MainWeatherModel {
		return szApi.getMainWeather(
			mapOf(
				"cityid" to cityId,
				"obtid" to stationId,
				"rainm" to Api2.RAINM,
				"uid" to Api2.UID,
			)
		).data
	}

	override suspend fun getCityWeatherByCityId(
		cityId: String,
	): MainWeatherModel = szApi.getMainWeather(
		mapOf(
			"cityid" to cityId,
			"rainm" to Api2.RAINM,
			"uid" to Api2.UID,
		)
	).data

	/**
	 * 观测区县 + 每个区下对应的站点列表
	 * 服务器上，非广东城市的站点列表数据为 null
	 */
	override suspend fun getStationList(
		params: Map<String, String>,
	): List<DistrictStationModel> = szApi.getStationList(
		params
	).data

	/**
	 * 收藏-城市天气
	 * 收藏页面有两个请求接口
	 * 站点天气请求和首页是公用的  getMainWeather(params: WeatherParams)
	 */
	override suspend fun getFavoriteCityWeather(
		params: Map<String, String>,
	): List<ShenZhenFavoriteCityWeather> = szApi.getFavoriteCityWeather(
		params
	).data.list

	override suspend fun getProvinceCityList(
		params: ProvinceCityParams,
	): ProvinceCityModel = szApi.getCityList(params.uid).data

	override suspend fun getProvinceCityListFromAssets(
	): ProvinceCityModel {
		val cites = context.assets
			.open("city.json")
			.bufferedReader()
			.use { it.readText() }
		val json = Json { ignoreUnknownKeys = true }
		val model = json.decodeFromString<CommonResult<ProvinceCityModel>>(cites)
		return model.data
	}
}
