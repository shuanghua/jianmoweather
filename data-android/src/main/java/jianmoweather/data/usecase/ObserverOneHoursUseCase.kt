package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.OneHour
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