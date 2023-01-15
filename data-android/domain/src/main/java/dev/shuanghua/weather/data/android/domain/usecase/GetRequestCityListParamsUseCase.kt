package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.CityListParams
import dev.shuanghua.weather.data.android.model.request.CityScreenRequest
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import javax.inject.Inject

class GetRequestCityListParamsUseCase @Inject constructor(
    private val paramsRepository: ParamsRepository
) {
    operator fun invoke(provinceId: String): String {
        val mainParams = paramsRepository.getRequestParams()
        val outerParams = mainParams.outerParams
        val cityListParams = CityListParams(
            provId = provinceId,
            cityids = mainParams.innerParams.cityids
        )
        return paramsRepository.getCityListRequestParams(
            CityScreenRequest(
                innerParams = cityListParams,
                outerParams = outerParams
            )
        )
    }
}