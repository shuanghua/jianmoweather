package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.repository.DistrictStationRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

class UpdateDistrictUseCase(
	private val dispatchers: AppDispatchers,
	private val districtStationRepository: DistrictStationRepository,
	private val paramsRepository: ParamsRepository
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

    data class Params(val cityId: String)

    override suspend fun doWork(params: Params): Unit =
        withContext(dispatchers.io) {
            paramsRepository.getWeatherParams().apply {
                DistrictParams(
                    cityId = params.cityId,
                    longitude = longitude,
                    latitude = latitude
                ).let { districtStationRepository.updateDistrictStationData(it) }
            }
        }
}