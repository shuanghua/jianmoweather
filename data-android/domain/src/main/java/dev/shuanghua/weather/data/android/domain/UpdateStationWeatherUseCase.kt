package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

/**
 * 从站点页面选择站点后，更新天气信息
 * 用户手动选择了其他站点，这就不需要定位相关的数据了
 * 因为定位方案，服务器自己会根据用户的位置返回最适合的站点
 */
class UpdateStationWeatherUseCase(
	private val weatherRepository: WeatherRepository, // 获取天气
	private val dispatchers: AppDispatchers,
) : UpdateUseCase<UpdateStationWeatherUseCase.Params>() {

	data class Params(val cityId: String, val stationId: String)

	override suspend fun doWork(
		params: Params,
	): Unit = withContext(dispatchers.io) {
		weatherRepository.updateCityOrStationWeather(
			params.cityId, params.stationId
		)
	}
}

internal fun createStationWeatherParamMap(
	cityId: String,
	stationId: String,
) = mapOf(
	"cityid" to cityId,
	"obtid" to stationId,
	"rainm" to "1",
	"uid" to "MV14fND4imK31h2Hsztq"
)