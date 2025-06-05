package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.UpdateUseCase


/**
 * UseCase 应该考虑重用性和复杂性(当某一个功能过于复杂, 放在 ViewModel 中就会影响可读性)
 * UseCase 利用了 operator invoke() 特性, 所以只能包含单一功能职责, 并要求轻量和简单
 * UseCase 可以注入别的 UseCase
 *
 * Get 开头的命名是直接从网络获取, 不保存到数据库
 * Update 开头的命名是从网络获取并更新数据库中的数据
 * lon=&lat=&cityIds=30120659033,28060159493,32010145005,28010159287,02010058362,01010054511&uid=MV14fND4imK31h2Hsztq
 */
class UpdateFavoriteCitiesWeatherUseCase(
	private val favoriteRepository: FavoritesRepository,
) : UpdateUseCase<List<FavoriteCity>>() {

	override suspend fun doWork(params: List<FavoriteCity>) {
		if (params.isEmpty()) {
			favoriteRepository.clearAllFavoriteCitiesWeather()
		} else {
			favoriteRepository.updateFavoriteCitiesWeather(
				params,
				createFavoriteCitiesWeatherMap(params)
			)
		}
	}
}

internal fun createFavoriteCitiesWeatherMap(
	favoriteCities: List<FavoriteCity>,
): Map<String, String> {
	val cityIds = favoriteCities.joinToString(separator = ",") { it.cityId } // 生成 id 字符串
	return mapOf(
		"lon" to "",
		"lat" to "",
		"cityIds" to cityIds,
		"uid" to "MV14fND4imK31h2Hsztq",
	)
}
