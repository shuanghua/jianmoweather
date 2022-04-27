package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.OneDay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverOneDaysUseCase @Inject constructor(
    private val weatherDao: WeatherDao
) : ObservableUseCase<ObserverOneDaysUseCase.Params, List<OneDay>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<OneDay>> {
        return weatherDao.findOneDays(params.cityId)
    }
}