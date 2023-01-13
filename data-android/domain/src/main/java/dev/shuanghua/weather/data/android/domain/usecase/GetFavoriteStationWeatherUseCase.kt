package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams
import dev.shuanghua.weather.data.android.model.asMainWeatherParam
import dev.shuanghua.weather.data.android.model.asOuterParams
import dev.shuanghua.weather.data.android.model.request.WeatherScreenRequest
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.convert.toInnerParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * 返回三种结果：
 * 1：成功     网络请求成功
 * 2：错误     网络请求失败
 * 3：空集合   数据库为空时
 */
class GetFavoriteStationWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
) {
    suspend operator fun invoke(): Flow<List<FavoriteStation>> {
        return favoriteRepository
            .observerFavoriteStationParams()
            .map { getStationWeather(it) }
    }

    private suspend fun getStationWeather(
        paramsList: List<FavoriteStationWeatherParams>
    ): List<FavoriteStation> {
        if (paramsList.isEmpty()) return emptyList()
        val jsonBodyList: List<String> = paramsList.map { createJsonBody(it) }
        return favoriteRepository.getMultipleStationsWeather(jsonBodyList)
    }

    private fun createJsonBody(params: FavoriteStationWeatherParams): String {
        val innerParams = params.toInnerParams().asMainWeatherParam()
        val outerParams = innerParams.asOuterParams()
        val weatherParams = WeatherScreenRequest(innerParams, outerParams)
        return paramsRepository.getMainWeatherRequestParams(weatherParams)
    }
}