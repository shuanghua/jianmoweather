package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import javax.inject.Inject

class SaveCityToFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoritesRepository
) : UpdateUseCase<String>() {

    override suspend fun doWork(params: String) {
        favoriteRepository.saveFavoriteCity(params)
    }
}