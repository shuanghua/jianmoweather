package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.Station
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverStationListUseCase @Inject constructor(
    private val stationRepository: StationRepository
) : ObservableUseCase<String, List<Station>>() {
    override fun createObservable(params: String): Flow<List<Station>> {
        return stationRepository.observerStationList(params)
    }
}