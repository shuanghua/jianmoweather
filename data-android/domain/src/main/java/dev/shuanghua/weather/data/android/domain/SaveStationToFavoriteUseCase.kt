package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteStationParams
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.StationRepository
import timber.log.Timber

/**
 * 实际上保存的是该站点对应的请求参数: 站点名 + 对应城市ID
 * 方便在收藏页面直接使用该参数请求天气数据
 */
class SaveStationToFavoriteUseCase(
	private val favoriteRepository: FavoritesRepository,
	private val stationRepository: StationRepository,
	private val locationRepository: LocationRepository,
) {
	suspend operator fun invoke(
		cityId: String,
		stationName: String,
	) {
		// 因为服务器接口现在不返回站点id了，所以只能通过站点名来获取站点id
		// 但如果用户没有打开过站点列表，也无法通过站点名来获取站点id
		// 所以需要改成存储请求参数
		Timber.d("SaveStationToFavoriteUseCase 保存站点: $cityId, $stationName")

		val location = locationRepository.getLocationFromDataStore()

		val stationId = stationRepository.getStationIdByName(stationName)
		val favoriteStationParams = if (stationId == null) {
			FavoriteStationParams(
				isAutoLocation = "1",
				cityId = cityId,
				stationName = stationName,
				latitude = location.latitude,
				longitude = location.longitude,
				cityName = location.cityName,
				district = location.district
			)
		} else {
			FavoriteStationParams(
				isAutoLocation = "0",
				cityId = cityId,
				stationName = stationName,
				stationId = stationId
			)
		}

		favoriteRepository.saveFavoriteStation(favoriteStationParams)
	}
}