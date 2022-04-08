package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.WeatherScreenEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class ObserverWeatherUseCase @Inject constructor(
//    private val weatherDao: WeatherDao,
//    dispatcher: AppCoroutineDispatchers
//) : FlowUseCase<ObserverWeatherUseCase.Params, Weather?>(dispatcher) {
//
//    override fun execute(parameters: Params): Flow<Weather?> {
//        return weatherDao.findWeather(parameters.cityId)
//    }
//
//    data class Params(val cityId: String)
//}

class ObserverTemperatureUseCase @Inject constructor(
    private val weatherDao: WeatherDao
) : ObservableUseCase<ObserverTemperatureUseCase.Params, WeatherScreenEntity?>() {

    data class Params(val screen: String)

    override fun createObservable(params: Params): Flow<WeatherScreenEntity?> {
        return weatherDao.findTemperature(params.screen)
    }
}