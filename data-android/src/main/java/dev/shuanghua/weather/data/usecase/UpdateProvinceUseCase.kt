package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.repo.province.ProvinceRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateProvinceUseCase @Inject constructor(
    private val provinceRepository: ProvinceRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<Unit>() {

    override suspend fun doWork(params: Unit) {

        withContext(dispatchers.io) {
            try {
                provinceRepository.updateProvince()
            } catch (t: Throwable) {
                Timber.e("---------------error---->>$t")
            }
        }
    }
}
