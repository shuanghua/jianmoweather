package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * 首页天气数据获取（包含定位），网络请求->保存数据库
 */
class UpdateWeatherUseCase(
	private val locationRepository: LocationRepository, // 定位
	private val paramsRepository: ParamsRepository, // 转换参数
	private val weatherRepository: WeatherRepository, // 获取天气
	private val dispatchers: AppDispatchers,
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

	// 首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
	data class Params(val cityId: String, val selectedStation: SelectedStation)

	override suspend fun doWork(
		params: Params,
	): Unit = withContext(dispatchers.io) {

		// 获取当前定位
		val networkLocationDeferred = async { locationRepository.getNetworkLocation() }
		val offlineLocationDeferred = async { locationRepository.getLocalLocation() }

		val (networkLocation, offlineLocation) = awaitAll(
			networkLocationDeferred, offlineLocationDeferred
		)

		// 获取当前所在站点
		val station = if (offlineLocation.cityName != networkLocation.cityName) {
			SelectedStation("", "1") // 跨越城市，强制按新的城市站点
		} else {
			params.selectedStation
		}

		// 请求参数
		val weatherParams = WeatherParams(
			longitude = networkLocation.longitude,
			latitude = networkLocation.latitude,
			cityId = params.cityId,    // 首次安装提供一个默认城市 id，之后的ID都是从天气返回中的 cityId 获取
			obtId = station.obtId,      //自动定位不需要传入具体的站点id
			cityName = networkLocation.cityName,
			district = networkLocation.district
		).also {
			paramsRepository.setWeatherParams(it)
		}

		// 开始网络请求
		weatherRepository.updateWeather(weatherParams)
		locationRepository.saveLocationToDataStore(networkLocation)
	}

}


// 笔记: 如果使用 !== , 当结构一样, 地址不一样, 比如 String a = “AB” , String b = “AB”
// a !== b 则返回 true , 所以 !== 是用来比较地址