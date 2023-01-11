package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverSelectedStation @Inject constructor(
    private val stationRepository: StationRepository
) : ObservableUseCase<Unit, SelectedStation?>() {

    override fun createObservable(params: Unit): Flow<SelectedStation?> {
        return stationRepository.getSelectedStation()
    }
}