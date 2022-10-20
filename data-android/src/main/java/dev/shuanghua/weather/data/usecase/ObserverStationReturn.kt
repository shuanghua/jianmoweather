package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.StationReturn
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverStationReturn @Inject constructor(
    private val stationDao: StationDao
) : ObservableUseCase<Unit, StationReturn?>() {

    override fun createObservable(params: Unit): Flow<StationReturn?> {
        return stationDao.observerStationReturn()
    }
}