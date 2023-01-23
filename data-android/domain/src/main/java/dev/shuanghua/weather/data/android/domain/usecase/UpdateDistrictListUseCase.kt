package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.repository.DistrictRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateDistrictListUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val districtRepository: DistrictRepository,
    private val paramsRepository: ParamsRepository,
) : UpdateUseCase<UpdateDistrictListUseCase.Params>() {

    data class Params(val cityId: String, val obtId: String)

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        //TODO
        val start = System.currentTimeMillis()
        val weatherParams = paramsRepository.getWeatherParams()

        val districtParams = DistrictParams(
            cityId = params.cityId,
            obtId = params.obtId,
            lon = weatherParams.lon,
            lat = weatherParams.lat
        )

        val districtJson = paramsRepository.districtListParamsToJson(districtParams)

        val end = System.currentTimeMillis()
        Timber.e("------序列化耗时>>${end - start}ms")

        districtRepository.updateStationList(districtJson)
    }

}