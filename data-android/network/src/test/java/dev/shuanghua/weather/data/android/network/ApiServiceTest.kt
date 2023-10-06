package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.model.CommonResult
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * WeatherApi
 */
interface ApiServiceTest {
	@POST("sztq-app/v6/client/index")
	suspend fun getMainWeather2(@Body data: MainWeatherRequest): CommonResult<MainWeatherModel>
}


