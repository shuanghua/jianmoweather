package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.pojo.PackingWeather
import dev.shuanghua.weather.data.model.WeatherResource
import dev.shuanghua.weather.data.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserverWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ObservableUseCase<Unit, WeatherResource>() {

    data class Params(val screen: String)

    override fun createObservable(params: Unit): Flow<WeatherResource> {
        return weatherRepository.getWeather()
    }
}