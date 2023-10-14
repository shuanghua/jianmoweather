package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.model.params.WeatherParams

class ParamsRepository {

	//用于非天气页面请求（需要使用里面的经纬度）
	private var weatherParams: WeatherParams? = null

	fun setWeatherParams(params: WeatherParams) {
		this.weatherParams = params
	}

	fun getWeatherParams(): WeatherParams {
		return weatherParams ?: throw NullPointerException(
			"ParamsRepository -> weatherParams 没有初始化！"
		)
	}
}