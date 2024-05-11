package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * UseCase 应该考虑重用性和复杂性(当某一个功能过于复杂, 放在 ViewModel 中就会影响可读性)
 * UseCase 利用了 operator invoke() 特性, 所以只能包含单一功能职责, 并要求轻量和简单
 * UseCase 可以注入别的 UseCase
 *
 * Get开头的命名是直接从网络获取, 不保存到数据库
 * 返回三种结果：
 * 1：成功     网络请求成功
 * 2：错误     网络请求失败
 * 3：空集合   数据库为空时
 */
class GetFavoriteCityWeatherUseCase(
	private val favoriteRepository: FavoritesRepository,
	private val paramsRepository: ParamsRepository,
) {
	suspend operator fun invoke(): Flow<List<FavoriteCity>> {
		return favoriteRepository.observerCityIds().map { getFavoriteCityWeather(it) }
	}

	private suspend fun getFavoriteCityWeather(ids: ArrayList<String>): List<FavoriteCity> {
		if (ids.isEmpty()) return emptyList()  // 当没有收藏城市时，清空 Ui

		val cityIds = ids.joinToString(separator = ",") // array to string

		val weatherParams = paramsRepository.getWeatherParams() // 获取其中的定位信息
		val requestParams = FavoriteCityParams(
			cityIds = cityIds,
			latitude = weatherParams.latitude,
			longitude = weatherParams.longitude,
		)

		return favoriteRepository.getFavoriteCityWeather(requestParams)
	}
}