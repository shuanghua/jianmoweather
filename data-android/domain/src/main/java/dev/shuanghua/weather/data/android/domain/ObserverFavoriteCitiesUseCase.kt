package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow


class ObserverFavoriteCitiesUseCase(
	private val favoriteRepository: FavoritesRepository,
) {
	suspend operator fun invoke(): Flow<List<FavoriteCity>> {
		return favoriteRepository.observerFavoriteCities()
	}
}