package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import javax.inject.Inject

class SaveCityToFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : UpdateUseCase<String>() {

    override suspend fun doWork(params: String) {
        favoriteRepository.saveFavoriteCity(params)
    }
}