package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.repo.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateFavoriteCityWeatherUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<String>() {
    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            try {
                favoriteRepository.updateFavoriteCityWeather(params)
            } catch (t: Throwable) {
                Timber.e("error->>$t")
            }
        }
    }
}
