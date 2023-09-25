package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.model.ShenZhenReturnData
import dev.shuanghua.weather.data.android.network.model.SzwModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * WeatherApi
 */
interface ApiServiceTest {
	@POST("sztq-app/v6/client/index")
	suspend fun getMainWeather2(@Body data: MainWeatherRequest): ShenZhenReturnData<SzwModel>
}


