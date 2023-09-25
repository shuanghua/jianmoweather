package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 首页天气数据获取（包含定位），网络请求->保存数据库
 */
class UpdateWeatherUseCase @Inject constructor(
	private val locationRepository: LocationRepository, // 定位
	private val paramsRepository: ParamsRepository, // 转换参数
	private val weatherRepository: WeatherRepository, // 获取天气
	private val dispatchers: AppCoroutineDispatchers,
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

	// 首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
	data class Params(val cityId: String, val selectedStation: SelectedStation)

	override suspend fun doWork(
		params: Params,
	): Unit = withContext(dispatchers.io) {

		// 定位 并行
		val networkLocationDeferred = async { locationRepository.getNetworkLocation() }
		val offlineLocationDeferred = async { locationRepository.getLocalLocation() }

		val networkLocation = networkLocationDeferred.await() // 当前定位
		val offlineLocation = offlineLocationDeferred.await() // 上一次定位

		// 站点
		val station =
			if (offlineLocation.cityName != networkLocation.cityName) {
				SelectedStation("", "1") // 跨越城市，强制按新的城市站点
			} else {
				params.selectedStation
			}

		// 首次安装 cityId 为 "" 时，需提供一个默认城市 id，这样才能根据定位的经纬度信息请求所在城市天气
		val cityId = params.cityId.ifEmpty { "28060159493" }


		// 请求
		launch {
			weatherRepository.updateWeather2(
				longitude = networkLocation.longitude,
				latitude = networkLocation.latitude,
				cityId = cityId,    // 首次安装提供一个默认城市 id，之后的ID都是从天气返回中的 cityId 获取
				obtId = station.obtId,      //自动定位不需要传入具体的站点id
				cityName = networkLocation.cityName,
				district = networkLocation.district
			)
		}

		launch { locationRepository.saveLocationToDataStore(networkLocation) }
	}

}


// 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
// a !== b 则返回 true ,所以 !== 是用来比较地址