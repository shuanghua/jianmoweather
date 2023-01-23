package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import javax.inject.Inject

class GetCityListRequestJsonUseCase @Inject constructor(
    private val paramsRepository: ParamsRepository
) {
    operator fun invoke(provinceId: String): String {
        val weatherParams = paramsRepository.getWeatherParams()
        val cityListParams = CityListParams(
            provId = provinceId,
            cityIds = weatherParams.cityIds
        )
        return paramsRepository.cityListParamsToJson(cityListParams)
    }
}