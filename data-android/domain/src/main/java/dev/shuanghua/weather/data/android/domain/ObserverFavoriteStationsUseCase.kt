package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.Province
import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow


class ObserverFavoriteStationsUseCase(
	private val provinceRepository: ProvinceCityRepository
) : ObservableUseCase<Unit, List<Province>>() {

	override fun createObservable(params: Unit): Flow<List<Province>> {
		return provinceRepository.observerProvinces()
	}
}