package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverFavoriteCityWeatherUseCase @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : ObservableUseCase<String, List<FavoriteCityWeather>>() {
    override fun createObservable(params: String): Flow<List<FavoriteCityWeather>> {
        return favoriteDao.observerCityWeather()
    }
}