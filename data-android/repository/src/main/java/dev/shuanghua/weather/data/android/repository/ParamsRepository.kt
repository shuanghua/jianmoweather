package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.model.params.WeatherParams

class ParamsRepository {

	// 用于非天气页面请求（需要使用里面的经纬度）
	private var weatherParams: WeatherParams = WeatherParams(
		cityName = "深圳市", // 默认站点农园
		cityId = "28060159493",
		district = "福田区",
		latitude = "22.546054",
		longitude = "114.025974"
	)

	fun updateWeatherParams(params: WeatherParams) {
		this.weatherParams = params
	}

	fun getWeatherParams(): WeatherParams {
		return weatherParams
	}
}