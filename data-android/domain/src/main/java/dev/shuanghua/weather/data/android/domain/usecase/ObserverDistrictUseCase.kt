package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.District
import dev.shuanghua.weather.data.android.repository.DistrictRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverDistrictUseCase @Inject constructor(
    private val districtRepository: DistrictRepository,
) : ObservableUseCase<Unit, List<District>>() {
    override fun createObservable(params: Unit): Flow<List<District>> {
        return districtRepository.observerDistricts()
    }
}