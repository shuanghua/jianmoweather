package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.entity.CityEntity
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.CityRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UseCase2
import timber.log.Timber
import javax.inject.Inject

/**
 * 根据省份ID获取城市列表
 */
class GetCityListUseCase @Inject constructor(
    private val paramsRepository: ParamsRepository,
    private val cityRepository: CityRepository,
    dispatchers: AppCoroutineDispatchers
) : UseCase2<String, List<CityEntity>>(dispatchers.io) {

    override suspend fun doWork(params: String): List<CityEntity> {
        val requestParam =  paramsRepository.getCityListByProvinceIdJson(params)
        Timber.d("-------$requestParam-----")
        return cityRepository.getCityListByProvinceId(requestParam)!!
    }
}