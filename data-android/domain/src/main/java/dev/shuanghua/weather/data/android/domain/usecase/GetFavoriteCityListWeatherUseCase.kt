package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.model.asFavoriteCityWeatherParams
import dev.shuanghua.weather.data.android.model.asOuterParams
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenCityRequest
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCityListWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
) {
    operator fun invoke(ids: ArrayList<String>): Flow<List<FavoriteCityWeather>> {
        return favoriteRepository.getNetworkCityWeather(toNetworkRequestJson(ids))
    }

    private fun toNetworkRequestJson(ids: ArrayList<String>): String {
        val cityIds = ids.joinToString(separator = ",")
        val innerParam = paramsRepository.getRequestParams().innerParams.copy(cityids = cityIds)
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