package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveCityToFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<String>() {

    override suspend fun doWork(params: String) = withContext(dispatchers.io) {
        favoriteRepository.saveFavoriteCity(params)
    }

}