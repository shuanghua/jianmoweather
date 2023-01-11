package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import dev.shuanghua.weather.data.android.repository.convert.asAlarmEntityList
import dev.shuanghua.weather.data.android.repository.convert.asConditionEntityList
import dev.shuanghua.weather.data.android.repository.convert.asExponentEntityList
import dev.shuanghua.weather.data.android.repository.convert.asOneDayEntityList
import dev.shuanghua.weather.data.android.repository.convert.asOneHourEntityList
import dev.shuanghua.weather.data.android.repository.convert.asWeather
import dev.shuanghua.weather.data.android.repository.convert.asWeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val network: NetworkDataSource
) {

    suspend fun getNetworkWeather(params: String): Flow<Weather> = flow {
        val networkData = network.getMainWeather(params) ?: throw Exception("服务器数据为空！")
        networkData.asWeather()
    }

    fun getOfflineWeather(): Flow<Weather> {
        val weatherResource = weatherDao.getWeather()
        return weatherResource.map {
            it?.asExternalModel() ?: emptyWeather
        }
    }

    /**
     * 首页定位城市调用(保存数据库)
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     */
    suspend fun updateWeather(params: String) {
        val networkData: ShenZhenWeather =
            network.getMainWeather(params) ?: throw Exception("服务器数据为空！")
        weatherDao.insertWeather(
            weatherEntity = networkData.asWeatherEntity(),
            listAlarm = networkData.asAlarmEntityList(),
            listOneDay = networkData.asOneDayEntityList(),
            listOnHour = networkData.asOneHourEntityList(),
            listCondition = networkData.asConditionEntityList(),
            listExponent = networkData.asExponentEntityList()
        )
    }

}