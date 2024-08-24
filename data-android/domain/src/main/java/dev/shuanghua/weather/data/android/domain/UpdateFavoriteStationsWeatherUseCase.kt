package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteStationParams
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.UpdateUseCase

class UpdateFavoriteStationsWeatherUseCase(
	private val favoriteRepository: FavoritesRepository,
) : UpdateUseCase<List<FavoriteStationParams>>() {

	/**
	 *  params: List<FavoriteStationParams> 从首页添加的站点的请求参数
	 */
	override suspend fun doWork(params: List<FavoriteStationParams>) {
		if (params.isEmpty()) {
			favoriteRepository.clearAllFavoriteStationsWeather() //删除数据库中的界面数据
		} else { // 从服务器获取所有 站点 天气数据
			favoriteRepository.updateFavoriteStationsWeather(params)
		}
	}
}