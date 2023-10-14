package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository

/**
 * 实际上保存的是该站点对应的请求参数: 站点名 + 对应城市ID
 * 方便在收藏页面直接使用该参数请求天气数据
 */
class SaveStationToFavorite(
	private val paramsRepository: ParamsRepository,
	private val favoriteRepository: FavoritesRepository,
) {
	suspend operator fun invoke(cityId: String, stationName: String) {
		val weatherParams = paramsRepository.getWeatherParams().copy(cityId = cityId)
		favoriteRepository.saveStationParam(weatherParams, stationName)
	}
}