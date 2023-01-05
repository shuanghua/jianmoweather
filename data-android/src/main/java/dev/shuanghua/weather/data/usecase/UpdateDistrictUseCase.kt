package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.network.DistrictParam
import dev.shuanghua.weather.data.network.asOuterParam
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
    private val paramsRepository: ParamsRepository,
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

    data class Params(val cityId: String, val obtId: String)

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val innerParam = paramsRepository.getInnerParam()

            val districtInnerParam = DistrictParam(
                cityid = params.cityId,
                obtid = params.obtId,
                lon = innerParam.lon,
                lat = innerParam.lat
            )
            Timber.e("DistrictScreenParam1:$districtInnerParam")

            val districtParam = paramsRepository.getDistrictParam(
                outer = innerParam.asOuterParam(),
                districtInnerParam
            )
            Timber.e("DistrictScreenParam:$districtParam")
            districtRepository.updateStationList(districtParam)
        }
    }

}