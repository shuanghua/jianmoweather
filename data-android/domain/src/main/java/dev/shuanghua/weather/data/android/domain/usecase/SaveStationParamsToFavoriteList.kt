package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.InnerParams
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveStationParamsToFavoriteList @Inject constructor(
    private val paramsRepository: ParamsRepository,
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<SaveStationParamsToFavoriteList.Params>() {

    data class Params(val cityId: String, val stationName: String)

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        val innerParam: InnerParams =
            paramsRepository.getRequestParams().innerParams.copy(cityid = params.cityId)
        favoriteRepository.saveStationParam(innerParam, params.stationName)
    }
}