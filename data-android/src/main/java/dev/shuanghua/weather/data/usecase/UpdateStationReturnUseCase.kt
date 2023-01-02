package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.SelectedStationEntity
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.CoroutineUseCase
import javax.inject.Inject

class UpdateStationReturnUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val stationDao: StationDao
) : CoroutineUseCase<UpdateStationReturnUseCase.Params, Unit>(dispatchers.io) {
    data class Params(val obtId: String, val isLocation: String)

    override suspend fun execute(params: Params) {
        val stationReturn = SelectedStationEntity("StationScreen", params.obtId, params.isLocation)
        stationDao.insertStationReturn(stationReturn)
    }
}