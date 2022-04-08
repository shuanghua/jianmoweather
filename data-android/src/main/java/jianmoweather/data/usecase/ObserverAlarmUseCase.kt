package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverAlarmUseCase @Inject constructor(
    val weatherDao: WeatherDao
) : ObservableUseCase<ObserverAlarmUseCase.Params, List<Alarm>>() {

    data class Params(val cityId: String)

    override fun createObservable(params: Params): Flow<List<Alarm>> {
        return weatherDao.findAlarms(params.cityId)
    }
}