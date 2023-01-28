package dev.shuanghua.weather.data.android.domain.usecase

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * 返回三种结果：
 * 1：成功     网络请求成功
 * 2：错误     网络请求失败
 * 3：空集合   数据库为空时
 */
class GetFavoriteCityListUseCase @Inject constructor(
    private val favoriteRepository: FavoritesRepository,
    private val paramsRepository: ParamsRepository,
) {
    suspend operator fun invoke(): Flow<List<FavoriteCity>> {
        return favoriteRepository.observerCityIds().map { getFavoriteCityWeather(it) }
    }

    private suspend fun getFavoriteCityWeather(ids: ArrayList<String>): List<FavoriteCity> {
        if (ids.isEmpty()) return emptyList()  // 当没有收藏城市是，清空 Ui
        val cityIds = ids.joinToString(separator = ",")
        val favoriteCityParams = createJsonBody(cityIds)
        return favoriteRepository.getFavoriteCityWeather(favoriteCityParams)
    }

    private fun createJsonBody(ids: String): FavoriteCityParams {
        val weatherParams = paramsRepository.getWeatherParams() // 获取其中的定位信息
        val favoriteCityParams = FavoriteCityParams(isAuto = "0", cityIds = ids)
        favoriteCityParams.cityName = weatherParams.cityName
        favoriteCityParams.district = weatherParams.district
        favoriteCityParams.lon = weatherParams.lon
        favoriteCityParams.lat = weatherParams.lat
        return favoriteCityParams
    }
}