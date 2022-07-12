package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : UpdateUseCase<AddFavoriteUseCase.Params>() {
    data class Params(val favorite:Favorite)

    override suspend fun doWork(params: Params) {
        favoriteRepository.addFavorite(params.favorite)
    }
}