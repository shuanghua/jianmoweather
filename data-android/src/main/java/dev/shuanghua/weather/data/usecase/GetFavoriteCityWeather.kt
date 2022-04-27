package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.network.OuterParam
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.CoroutineResultUseCase
import timber.log.Timber
import javax.inject.Inject

class GetFavoriteCityWeather @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
    dispatcher: AppCoroutineDispatchers,
) : CoroutineResultUseCase<Unit, Unit>(dispatcher) {


    override suspend fun execute(parameters: Unit?): Unit {
        val requestParam = paramsRepository.getFavoriteWeatherRequestJson(OuterParam.CITYIDS)
        val data = favoriteRepository.getFavoriteCityWeather(requestParam)
        Timber.d("------------------->>$requestParam")
        Timber.d("------------------->>${data?.list}")
    }
}
