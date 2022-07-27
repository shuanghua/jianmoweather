package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.DistrictDao
import dev.shuanghua.weather.data.db.entity.District
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverDistrictUseCase @Inject constructor(
    private val districtDao: DistrictDao
) : ObservableUseCase<Unit, List<District>>() {
    override fun createObservable(params: Unit): Flow<List<District>> {
        return districtDao.observerDistricts()
    }
}