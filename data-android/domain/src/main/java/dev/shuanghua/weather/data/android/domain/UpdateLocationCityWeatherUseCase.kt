package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase

/**
 * 首页天气数据获取（包含定位），网络请求->保存数据库
 * 	- 自动定位：成功和失败
 * 	- 定位成功：lat=0000000 & lon=0000000 & pcity=xxx市 & parea=xxxx区
 * 	- 定位失败：直接获取上次定位 或者 使用默认城市
 */
class UpdateLocationCityWeatherUseCase(
	private val locationRepository: LocationRepository, // 定位
	private val weatherRepository: WeatherRepository, // 获取天气
) : UpdateUseCase<Unit>() {
	override suspend fun doWork(params: Unit) {
		val location = locationRepository.getLocation()
		weatherRepository.updateLocationCityWeather(location)
	}
}