package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * 点击收藏页面 Item 打开 天气页面
 */
class GetStationOrCityWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoritesRepository,
    private val paramsRepository: ParamsRepository,
    private val dispatchers: AppCoroutineDispatchers
) {
    suspend operator fun invoke(
        cityId: String,
        stationName: String
    ): Flow<Weather> = flow {
        if (stationName == "Null") {  // 城市天气
            val weatherParams = paramsRepository
                .getWeatherParams()
                .copy(  // 不修改原来数据
                    cityId = cityId,
                    obtId = "",
                    cityIds = "",
                    isAuto = "0"
                )
            emit(favoriteRepository.getFavoritesWeather(weatherParams))
        } else {  // 站点天气
            val weatherParams = favoriteRepository.getStationParamsByName(stationName)
            emit(favoriteRepository.getFavoritesWeather(weatherParams))
        }
    }.flowOn(dispatchers.io)
}

