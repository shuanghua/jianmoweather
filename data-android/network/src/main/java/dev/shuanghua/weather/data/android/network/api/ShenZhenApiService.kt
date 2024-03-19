package dev.shuanghua.weather.data.android.network.api

import dev.shuanghua.weather.data.android.network.model.CommonResult
import dev.shuanghua.weather.data.android.network.model.DistrictStationModel
import dev.shuanghua.weather.data.android.network.model.FavoriteCityWeatherReturn
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
import dev.shuanghua.weather.data.android.network.model.ProvinceCityModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * WeatherApi
 */
interface ShenZhenApi {
	@POST("sztq-app/v6/client/index")
	suspend fun getMainWeather(
		@QueryMap map: Map<String, String>
	): CommonResult<MainWeatherModel>


	@POST("sztq-app/v6/client/autoStationList")
	suspend fun getStationList(
		@QueryMap map: Map<String, String>
	): CommonResult<List<DistrictStationModel>>


	@POST("sztq-app/v6/client/city/alreadyAddCityList")
	suspend fun getFavoriteCityWeather(
		@QueryMap data: Map<String, String>
	): CommonResult<FavoriteCityWeatherReturn>


	@GET("sztq-app/v6/client/city/list")
	suspend fun getCityList(
		@Query("uid") data: String,
	): CommonResult<ProvinceCityModel>
}



