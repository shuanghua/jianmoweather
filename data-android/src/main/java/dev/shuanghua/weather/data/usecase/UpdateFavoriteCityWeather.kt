package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateFavoriteCityWeather @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateFavoriteCityWeather.Params>() {

    data class Params(val cityIdList: ArrayList<String>)

    override suspend fun doWork(params: Params) {

        withContext(dispatchers.io) {
            try {
                val requestParam = paramsRepository.getFavoriteWeatherRequestJson(params.cityIdList)
                Timber.d("---------------params---->>$requestParam")
                favoriteRepository.updateFormNetwork(requestParam)
            }catch (t:Throwable){
                Timber.e("---------------error---->>$t")
            }
        }
    }
}
