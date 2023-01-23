package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.repository.CityRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCityListUseCase @Inject constructor(
    private val paramsRepository: ParamsRepository, // 转换参数
    private val cityRepository: CityRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateCityListUseCase.Params>() {

    data class Params(val provinceName: String, val provinceId: String)

    override suspend fun doWork(params: Params) =
        withContext(dispatchers.io) {
            val weatherParams = paramsRepository.getWeatherParams()
            val cityListParams = CityListParams(
                provId = params.provinceId,
                cityIds = weatherParams.cityIds
            )
            val paramsJson = paramsRepository.cityListParamsToJson(cityListParams)
            cityRepository.updateCityList(
                json = paramsJson,
                provinceName = params.provinceName
            )
        }
}