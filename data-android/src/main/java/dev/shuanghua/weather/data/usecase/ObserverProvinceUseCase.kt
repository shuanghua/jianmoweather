package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.ProvinceDao
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import dev.shuanghua.weather.data.db.entity.Province
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverProvinceUseCase @Inject constructor(
    private val provinceDao: ProvinceDao
) : ObservableUseCase<ObserverProvinceUseCase.Params, List<Province>>() {

    data class Params(val screen: String)

    override fun createObservable(params: Params): Flow<List<Province>> {
        return provinceDao.observerProvinces()
    }
}