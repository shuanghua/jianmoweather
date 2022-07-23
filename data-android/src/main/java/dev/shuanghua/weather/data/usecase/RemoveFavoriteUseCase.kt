package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.CoroutineUseCase
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val favoriteDao: FavoriteDao,
    dispatchers: AppCoroutineDispatchers
) : CoroutineUseCase<RemoveFavoriteUseCase.Params, Unit>(dispatchers.io) {
    data class Params(val cityId: String)

    override suspend fun execute(params: Params) {
        val favoriteCityId = FavoriteId(params.cityId)
        favoriteDao.deleteFavoriteId(favoriteCityId)
        favoriteDao.favoriteCityWeather(params.cityId)
    }
}