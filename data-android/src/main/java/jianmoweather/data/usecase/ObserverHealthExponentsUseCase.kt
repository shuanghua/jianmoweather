package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.HealthExponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverHealthExponentsUseCase @Inject constructor(
    val weatherDao: WeatherDao
) : ObservableUseCase<ObserverHealthExponentsUseCase.Params, List<HealthExponent>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<HealthExponent>> {
        return weatherDao.findHealthExponents(params.cityId)
    }
}