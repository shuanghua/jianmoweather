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
 * 点击收藏列表后的天气详情页面
 * 需要 cityId  或 stationId,取决于点击的是城市还是站点
 */
class GetFavoriteDetailWeatherUseCase(
	private val favoriteRepository: FavoritesRepository,
	private val dispatchers: AppDispatchers,
) {
	operator fun invoke(
		cityId: String,
		stationName: String,
	): Flow<Weather> = flow {
		Timber.d("GetFavoriteDetailWeatherUseCase: $cityId, $stationName")
		if (stationName == "Null") {  // 类型1：城市
			emit(favoriteRepository.getFavoriteCityDetailWeather(cityId))
		} else {  // 站点
			val stationParam: FavoriteStationParams =
				favoriteRepository.getFavoriteStationByName(stationName)
			val stationId = stationParam.stationId
			if (stationParam.isAutoLocation == "1") { //类型2： 自动站点，需要经纬度
				emit(
					favoriteRepository.getFavoriteLocationStationWeather(
						stationParam.latitude,
						stationParam.longitude,
						stationParam.cityName,
						stationParam.district
					)
				)
			} else { // 类型：3：手动站点，需要城市ID和站点ID
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

