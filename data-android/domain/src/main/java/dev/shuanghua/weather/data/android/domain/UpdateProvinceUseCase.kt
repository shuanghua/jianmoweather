package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

class UpdateProvinceUseCase(
	private val provinceCityRepository: ProvinceCityRepository,
	private val dispatchers: AppDispatchers
) : UpdateUseCase<Unit>() {
	override suspend fun doWork(params: Unit) {
		withContext(dispatchers.io) {
			provinceCityRepository.updateProvinceCityList()
		}
	}
}
