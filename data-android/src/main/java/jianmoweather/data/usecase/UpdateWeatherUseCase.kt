package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.AppCoroutineDispatchers
import com.moshuanghua.jianmoweather.shared.usecase.UpdateUseCase
import jianmoweather.data.repo.location.ParamsRepository
import jianmoweather.data.repo.weather.WeatherRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * (首页)定位城市数据获取
 */
class UpdateWeatherUseCase @Inject constructor(
    private val paramsRepository: ParamsRepository,
    private val weatherRepository: WeatherRepository,//in Params
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<String>() {

    override suspend fun doWork(screen: String) { // params = null 意味定位失败
        withContext(dispatchers.io) {
            weatherRepository.updateWeatherData(
                screen,
                paramsRepository.getRequestParams()
            )

        }
    }
}

