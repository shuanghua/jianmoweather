package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.asFavoriteCityWeatherParams
import dev.shuanghua.weather.data.android.model.asOuterParams
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenCityRequest
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * 返回三种结果：
 * 1：成功     网络请求成功
 * 2：错误     网络请求失败
 * 3：空集合   数据库为空时
 */
class GetFavoriteCityWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
) {
    suspend operator fun invoke(): Flow<List<FavoriteCity>> {
        return favoriteRepository.observerCityIds().map { getFavoriteCityWeather(it) }
    }

    private suspend fun getFavoriteCityWeather(ids: ArrayList<String>): List<FavoriteCity> {
        if (ids.isEmpty()) return emptyList()  // 当没有收藏城市是，清空 Ui
        val cityIds = ids.joinToString(separator = ",")
        val jsonBody = createJsonBody(cityIds)
        return favoriteRepository.getFavoriteCityWeather(jsonBody)
    }

    private fun createJsonBody(ids: String): String {
        val innerParam = paramsRepository.getRequestParams().innerParams.copy(cityids = ids)
        val favoriteCityWeatherParams = innerParam.asFavoriteCityWeatherParams()
        val outerParams = innerParam.asOuterParams()
        return paramsRepository.getFavoriteCityWeatherRequestParams(
            FavoriteScreenCityRequest(
                innerParams = favoriteCityWeatherParams,
                outerParams = outerParams
            )
        )
    }
}