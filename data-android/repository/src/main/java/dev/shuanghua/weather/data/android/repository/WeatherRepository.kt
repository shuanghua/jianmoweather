package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.NetworkModel
import dev.shuanghua.weather.data.android.network.SzwNetworkDataSource
import dev.shuanghua.weather.data.android.repository.converter.toEntities
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
	private val weatherDao: WeatherDao,
	private val szwNetworkDataSource: SzwNetworkDataSource,
	private val dispatchers: AppCoroutineDispatchers,
) {
	fun getOfflineWeather(): Flow<Weather> =
		weatherDao.getWeather().map {
			it?.asExternalModel() ?: emptyWeather
		}

	suspend fun updateWeather(params: WeatherParams) {
		// 以后这里可以获取其它接口数据,然后合并到 NetworkModel,再清洗整合成 App 需要的数据
		val szw = szwNetworkDataSource.getMainWeather(params)
		val networkData = NetworkModel(szw = szw)
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