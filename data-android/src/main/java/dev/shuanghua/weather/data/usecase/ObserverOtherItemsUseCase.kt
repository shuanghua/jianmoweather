package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.Condition
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverOtherItemsUseCase @Inject constructor(
    private val weatherDao: WeatherDao
) : ObservableUseCase<ObserverOtherItemsUseCase.Params, List<Condition>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<Condition>> {
        return weatherDao.findOtherItems(params.cityId)
    }
}