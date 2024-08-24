package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.District
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow

class ObserverDistrictUseCase(
	private val stationRepository: StationRepository,
) : ObservableUseCase<Unit, List<District>>() {

	override fun createObservable(params: Unit): Flow<List<District>> {
		return stationRepository.observerDistrictList()
	}
}