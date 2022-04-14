package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.pojo.Weather
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