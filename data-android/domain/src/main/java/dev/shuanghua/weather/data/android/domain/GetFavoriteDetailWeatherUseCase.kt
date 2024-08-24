package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteStationParams
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * 点击收藏页面的列表项
 */
class GetFavoriteDetailWeatherUseCase(
	private val favoriteRepository: FavoritesRepository,
	private val dispatchers: AppDispatchers,
) {
	suspend operator fun invoke(
		cityId: String,
		stationName: String,
	): Flow<Weather> = flow {
		Timber.d("GetFavoriteDetailWeatherUseCase: $cityId, $stationName")
		if (stationName == "Null") {  // 城市
			emit(favoriteRepository.getFavoriteCityDetailWeather(cityId))
		} else {  // 站点
			val stationParam: FavoriteStationParams =
				favoriteRepository.getFavoriteStationByName(stationName)
			val stationId = stationParam.stationId
			if (stationParam.isAutoLocation == "1") { // 需要经纬度
				emit(
					favoriteRepository.getFavoriteLocationStationWeather(
						stationParam.latitude,
						stationParam.longitude,
						stationParam.cityName,
						stationParam.district
					)
				)
			} else { // 需要城市ID和站点ID
				emit(
					favoriteRepository.getFavoriteStationWeather(
						cityId,
						stationId
					)
				)
			}
		}
	}.flowOn(dispatchers.io)
}

