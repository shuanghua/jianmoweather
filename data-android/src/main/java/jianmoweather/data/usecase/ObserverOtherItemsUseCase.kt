package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.Condition
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