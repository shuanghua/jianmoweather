package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.SelectedStationEntity
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverStationReturn @Inject constructor(
    private val stationDao: StationDao
) : ObservableUseCase<Unit, SelectedStationEntity?>() {

    override fun createObservable(params: Unit): Flow<SelectedStationEntity?> {
        return stationDao.observerSelectedStation()
    }
}