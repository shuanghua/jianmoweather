package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.DistrictParams
import dev.shuanghua.weather.data.android.model.request.DistrictScreenRequest
import dev.shuanghua.weather.data.android.repository.DistrictRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDistrictUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val districtRepository: DistrictRepository,
    private val paramsRepository: ParamsRepository,
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

    data class Params(val cityId: String, val obtId: String)

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        val mainParams = paramsRepository.getRequestParams()
        val innerParams = mainParams.innerParams
        val outerParams = mainParams.outerParams

        val districtInnerParam = DistrictParams(
            cityid = params.cityId,
            obtid = params.obtId,
            lon = innerParams.lon,
            lat = innerParams.lat
        )

        val districtJson = paramsRepository.getDistrictListRequestParams(
            DistrictScreenRequest(districtInnerParam, outerParams)
        )

        districtRepository.updateStationList(districtJson)
    }


}