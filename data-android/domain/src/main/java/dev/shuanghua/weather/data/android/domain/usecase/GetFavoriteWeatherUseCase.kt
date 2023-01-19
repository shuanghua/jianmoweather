package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.asMainWeatherParam
import dev.shuanghua.weather.data.android.model.asOuterParams
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenStationRequest
import dev.shuanghua.weather.data.android.model.request.WeatherScreenRequest
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.convert.asInnerParams
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GetFavoriteWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoritesRepository,
    private val paramsRepository: ParamsRepository,
    private val dispatchers: AppCoroutineDispatchers
) {
    suspend operator fun invoke(cityId: String, stationName: String): Flow<Weather> = flow {
        Timber.e("->>>>>$cityId, $stationName")
        if (stationName == "Null") {  // 城市天气
            val params = paramsRepository.getRequestParams()
            val fullParams = WeatherScreenRequest(
                innerParams = params.innerParams.asMainWeatherParam().copy(
                    cityid = cityId,
                    obtId = "",
                    isauto = "0",
                    cityids = ""
                ),
                outerParams = params.outerParams
            )
            val jsonBody = paramsRepository.getMainWeatherRequestParams(fullParams)
            emit(favoriteRepository.getFavoritesWeather(jsonBody))
        } else {  // 站点天气
            val stationParams: FavoriteStationWeatherParams =
                favoriteRepository.getStationParamsByStationName(stationName)
            val fullParams = FavoriteScreenStationRequest(
                innerParams = stationParams,
                outerParams = stationParams.asInnerParams().asOuterParams()
            )
            val jsonBody = paramsRepository.getFavoriteStationWeatherRequestParams(fullParams)
            emit(favoriteRepository.getFavoritesWeather(jsonBody))
        }
    }.flowOn(dispatchers.io)
}

