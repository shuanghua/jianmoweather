package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UseCase
import dev.shuanghua.weather.shared.usecase.UseCase2
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetFavoriteCityWeatherUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val favoriteDao: FavoriteDao,
    private val paramsRepository: ParamsRepository,
    private val favoriteRepository: FavoriteRepository,
) : UseCase2<ArrayList<String>, List<FavoriteCityWeather>>(dispatchers.io) {
    // data class Params() 当需要传多个参数时，使用P arams 类来包装
    override suspend fun doWork(params: ArrayList<String>): List<FavoriteCityWeather> {
//        favoriteDao.observerFavoriteId().map {
            val requestParam = paramsRepository.getFavoriteWeatherRequestJson(params)
            Timber.d("---------------params---->>$requestParam")
            return favoriteRepository.getFavoriteCityWeather(requestParam)
        }
//    }
}