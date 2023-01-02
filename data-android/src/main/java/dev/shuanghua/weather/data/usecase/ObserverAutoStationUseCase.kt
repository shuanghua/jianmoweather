package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.AutoLocationStation
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverAutoStationUseCase @Inject constructor(
    private val stationDao: StationDao
) : ObservableUseCase<Unit, AutoLocationStation>() {
    override fun createObservable(params: Unit): Flow<AutoLocationStation> {
        return stationDao.observerAutoStation()
    }
}