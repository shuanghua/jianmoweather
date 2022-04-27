package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.Exponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverHealthExponentsUseCase @Inject constructor(
    val weatherDao: WeatherDao
) : ObservableUseCase<ObserverHealthExponentsUseCase.Params, List<Exponent>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<Exponent>> {
        return weatherDao.findHealthExponents(params.cityId)
    }
}