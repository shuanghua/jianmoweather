package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.repository.ProvinceRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateProvinceUseCase @Inject constructor(
    private val provinceRepository: ProvinceRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            provinceRepository.updateProvince()
        }
    }
}
