package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.AppCoroutineDispatchers
import com.moshuanghua.jianmoweather.shared.usecase.UpdateUseCase
import jianmoweather.data.repo.location.LocationRepository
import jianmoweather.data.repo.location.ParamsRepository
import jianmoweather.data.repo.weather.WeatherRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * (首页)定位城市数据获取
 * 定位 + 查询城市id + 获取天气数据
 */
class UpdateWeatherUseCase @Inject constructor(
	private val locationRepository: LocationRepository,
	private val paramsRepository: ParamsRepository,
	private val weatherRepository: WeatherRepository,//in Params
	private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

	data class Params(val screen: String)

	override suspend fun doWork(params: Params) { // params = null 意味定位失败
		withContext(dispatchers.io) {
			// 此处 requestParams 永远不为空, 因为可能返: 新的请求参数 或 缓存在数据库的上次请求参数 或 默认参数
			try {
				val location = locationRepository.getLocation() // 定位出错则抛异常
				val requestParams = paramsRepository.getRequestParams(location)
				weatherRepository.updateWeatherData(params.screen, requestParams)
			} catch(t: Throwable) {
				val requestParams = paramsRepository.getRequestParams(null)
				weatherRepository.updateWeatherData(params.screen, requestParams)
				throw t
			}
		}
	}
}