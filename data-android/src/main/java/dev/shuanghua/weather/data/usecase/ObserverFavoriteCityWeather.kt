package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverFavoriteCityWeather @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ObservableUseCase<ObserverFavoriteCityWeather.Params, List<Favorite>>() {
    data class Params(private val screen: String)

    override fun createObservable(params: Params): Flow<List<Favorite>> {
        return favoriteRepository.observerFavoriteCityWeather()
    }
}