package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.OneHour
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverOneHoursUseCase @Inject constructor(
    private val weatherDao: WeatherDao
) : ObservableUseCase<ObserverOneHoursUseCase.Params, List<OneHour>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<OneHour>> {
        return weatherDao.findOneHours(params.cityId)
    }
}