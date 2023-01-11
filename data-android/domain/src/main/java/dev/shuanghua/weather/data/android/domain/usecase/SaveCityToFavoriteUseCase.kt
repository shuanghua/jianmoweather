package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveCityToFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<City>() {

    override suspend fun doWork(params: City) {
        val favoriteCity = FavoriteCity(params.id, params.name)
        favoriteRepository.saveFavoriteCity(favoriteCity)
    }
}