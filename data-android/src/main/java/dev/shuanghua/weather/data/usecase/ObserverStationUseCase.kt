package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.Station
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverStationUseCase @Inject constructor(
    private val stationDao: StationDao
) : ObservableUseCase<String, List<Station>>() {
    override fun createObservable(params: String): Flow<List<Station>> {
        return stationDao.observerStations(params)
    }
}