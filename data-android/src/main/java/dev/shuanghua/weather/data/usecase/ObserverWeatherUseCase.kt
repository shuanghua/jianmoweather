package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.pojo.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverWeatherUseCase @Inject constructor(
    private val weatherDao: WeatherDao
) : ObservableUseCase<ObserverWeatherUseCase.Params, Weather?>() {

    data class Params(val screen: String)

    override fun createObservable(params: Params): Flow<Weather?> {
        return weatherDao.observerWeather(params.screen)
    }
}