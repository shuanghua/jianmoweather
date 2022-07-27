package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.network.DistrictParam
import dev.shuanghua.weather.data.repo.DistrictRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateDistrictUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val districtRepository: DistrictRepository,
    private val paramsRepository: ParamsRepository
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

    data class Params(val cityid: String, val obtid: String)

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val dp = DistrictParam(params.cityid, params.obtid)
            val requestParam: String = paramsRepository.getDistrictParam(dp)
            Timber.d(requestParam)
            districtRepository.updateStationList(requestParam)
        }
    }

}