package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveStationToFavoriteList @Inject constructor(
    private val paramsRepository: ParamsRepository,
    private val favoriteRepository: FavoritesRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<SaveStationToFavoriteList.Params>() {

    data class Params(val cityId: String, val stationName: String)

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        val weatherParams = paramsRepository.getWeatherParams().copy(cityId = params.cityId)
        favoriteRepository.saveStationParam(weatherParams, params.stationName)
    }
}