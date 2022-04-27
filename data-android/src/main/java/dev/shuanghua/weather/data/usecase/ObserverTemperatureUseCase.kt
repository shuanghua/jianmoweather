package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.Temperature
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
) : ObservableUseCase<ObserverTemperatureUseCase.Params, Temperature?>() {

    data class Params(val screen: String)

    override fun createObservable(params: Params): Flow<Temperature?> {
        return weatherDao.findTemperature(params.screen)
    }
}