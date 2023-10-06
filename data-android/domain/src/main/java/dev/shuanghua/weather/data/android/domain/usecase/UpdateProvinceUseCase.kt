package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateProvinceUseCase @Inject constructor(
    private val provinceCityRepository: ProvinceCityRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            provinceCityRepository.updateProvinceCityList()
        }
    }
}
