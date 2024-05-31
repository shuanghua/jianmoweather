package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.NetworkModel
import dev.shuanghua.weather.data.android.repository.converter.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface WeatherRepository {
	fun observerWeather(): Flow<Weather>
	suspend fun updateWeather(weatherParams: WeatherParams)
}

class WeatherRepositoryImpl(
	private val weatherDao: WeatherDao,
	private val szwNetworkDataSource: NetworkDataSource,
) : WeatherRepository {

	override fun observerWeather(): Flow<Weather> {
		return weatherDao.getWeather().map {
			it?.asExternalModel() ?: emptyWeather
		}
	}

	override suspend fun updateWeather(weatherParams: WeatherParams) {
		val szw = szwNetworkDataSource.getMainWeather(weatherParams) // 获取网络数据
		val networkData = NetworkModel(szw = szw) // 数据转换
		networkData.toEntities().apply {
			weatherDao.insertWeather(
				weatherEntity = weatherEntity,
				alarmEntities = alarmEntities,
				oneDayEntities = oneDayEntities,
				onHourEntities = onHourEntities,
				conditionEntities = conditionEntities,
				exponentEntities = exponentEntities,
			)
		}
	}
}