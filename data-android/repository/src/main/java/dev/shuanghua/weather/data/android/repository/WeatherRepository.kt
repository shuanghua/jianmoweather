package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.previewWeather
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.NetworkModel
import dev.shuanghua.weather.data.android.repository.converter.toEntities
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException

interface WeatherRepository {
	fun observerWeather(): Flow<Weather>

	suspend fun updateLocationCityWeather(location: Location)

	suspend fun updateCityOrStationWeather(
		cityId: String,
		stationId: String,
	)
}

class WeatherRepositoryImpl(
	private val weatherDao: WeatherDao,
	private val network: NetworkDataSource,
	private val dispatcher: AppDispatchers,
) : WeatherRepository {

	override fun observerWeather(): Flow<Weather> {
		return weatherDao.getWeather()
			.filterNotNull() // 空数据则不处理
			.map { it.asExternalModel() }
	}

	/**
	 * 自动定位城市更新天气
	 */
	override suspend fun updateLocationCityWeather(
		location: Location,
	) = withContext(dispatcher.io) {
		try {
			val szw = network.getCityWeatherByLocation(
				latitude = location.latitude,
				longitude = location.longitude,
				cityName = location.cityName,
				district = location.district,
			)
			val networkData = NetworkModel(szw = szw) // 数据转换
			weatherDao.insertWeatherEntities(networkData.toEntities())
		} catch (e: Exception) {
			Timber.e(e)
			val weather = weatherDao.getWeather().firstOrNull()
			if (weather == null) {
				weatherDao.insertWeatherEntities(previewWeather.toEntities())
			}
			when (e) { // 重复代码，待抽取
				is UnknownHostException -> throw UnknownHostException("首页定位更新：请检查网络或者使用中国地区VPN")
				else -> throw e
			}
		}
	}

	/**
	 * 手动选择城市或者站点更新天气
	 */
	override suspend fun updateCityOrStationWeather(
		cityId: String,
		stationId: String,
	) = withContext(dispatcher.io) {
		try {
			val szw = network.getCityWeatherByStationId(cityId, stationId) // 获取网络数据
			val networkData = NetworkModel(szw = szw) // 数据转换
			weatherDao.insertWeatherEntities(networkData.toEntities())
		} catch (e: Exception) {
			Timber.e(e)
			when (e) {
				is UnknownHostException -> throw UnknownHostException("首页站点更新：请检查网络或者使用中国地区VPN")
				else -> throw e
			}
		}
	}
}