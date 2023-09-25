package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.NetworkModel
import dev.shuanghua.weather.data.android.network.SzNetworkDataSource
import dev.shuanghua.weather.data.android.network.model.request.MainWeatherRequest
import dev.shuanghua.weather.data.android.repository.converter.toEntities
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
	private val weatherDao: WeatherDao,
	private val szwNetworkDataSource: SzNetworkDataSource,
	private val dispatchers: AppCoroutineDispatchers,
) {
	fun getOfflineWeather(): Flow<Weather> =
		weatherDao.getWeather().map {
			it?.asExternalModel() ?: emptyWeather
		}

	suspend fun updateWeather2(
		longitude: String,
		latitude: String,
		cityId: String,
		obtId: String,
		cityName: String,
		district: String,
	) {
		// 请求参数
		val request = MainWeatherRequest(
			lon = longitude,
			lat = latitude,
			cityid = cityId,    // 首次安装提供一个默认城市 id，之后的ID都是从天气返回中的 cityId 获取
			obtid = obtId,      //自动定位不需要传入具体的站点id
			pcity = cityName,
			parea = district
		)
		val szw = szwNetworkDataSource.getMainWeather2(request) // 获取网络数据
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