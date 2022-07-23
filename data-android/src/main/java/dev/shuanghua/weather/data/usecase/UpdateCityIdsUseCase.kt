package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import javax.inject.Inject

class UpdateCityIdsUseCase @Inject constructor(
    private val favoriteDao: FavoriteDao
): UpdateUseCase<String>(){
    override suspend fun doWork(params: String) {
        val favoriteCityId = FavoriteId(params)
        favoriteDao.insertFavoriteId(favoriteCityId)
    }
}
