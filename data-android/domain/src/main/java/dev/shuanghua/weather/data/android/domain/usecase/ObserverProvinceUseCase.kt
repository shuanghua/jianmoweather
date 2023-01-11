package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.shared.ObservableUseCase
import dev.shuanghua.weather.data.android.repository.ProvinceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserverProvinceUseCase @Inject constructor(
    private val provinceRepository: ProvinceRepository
) : ObservableUseCase<Unit, List<Province>>() {

    override fun createObservable(params: Unit): Flow<List<Province>> {
        return provinceRepository.observerProvinces()
    }
}