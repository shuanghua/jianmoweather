package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveSelectedStationUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val stationRepository: StationRepository
) : UpdateUseCase<SaveSelectedStationUseCase.Params>() {

    data class Params(val obtId: String)

    override suspend fun doWork(params: Params) =
        withContext(dispatchers.io) {
            val selectedStation = SelectedStation(
                obtId = params.obtId,
                isLocation = "1"   //返回到首页定位则传1，完美情况应该根据定位是否成功来判定
            )
            stationRepository.saveSelectedStation(selectedStation)
        }
}