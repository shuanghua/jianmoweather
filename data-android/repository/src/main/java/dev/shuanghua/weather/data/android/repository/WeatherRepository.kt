package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.NetworkModel
import dev.shuanghua.weather.data.android.network.SzNetworkDataSource
import dev.shuanghua.weather.data.android.repository.converter.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
	private val weatherDao: WeatherDao,
	private val szwNetworkDataSource: SzNetworkDataSource,
) {
	fun getOfflineWeather(): Flow<Weather> =
		weatherDao.getWeather().map {
			it?.asExternalModel() ?: emptyWeather
		}

	suspend fun updateWeather(weatherParams: WeatherParams) {
		println("参数:$weatherParams")
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