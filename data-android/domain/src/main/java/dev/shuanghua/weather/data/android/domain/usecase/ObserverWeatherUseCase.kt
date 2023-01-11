package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ObservableUseCase<Unit, Weather>() {

    override fun createObservable(params: Unit): Flow<Weather> {
        return weatherRepository.getOfflineWeather()
    }
}