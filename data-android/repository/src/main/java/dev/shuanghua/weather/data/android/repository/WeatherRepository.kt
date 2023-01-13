package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.pojo.asExternalModel
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.emptyWeather
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.ShenZhenWeather
import dev.shuanghua.weather.data.android.repository.convert.asAlarmEntityList
import dev.shuanghua.weather.data.android.repository.convert.asConditionEntityList
import dev.shuanghua.weather.data.android.repository.convert.asExponentEntityList
import dev.shuanghua.weather.data.android.repository.convert.asFavoriteStation
import dev.shuanghua.weather.data.android.repository.convert.asOneDayEntityList
import dev.shuanghua.weather.data.android.repository.convert.asOneHourEntityList
import dev.shuanghua.weather.data.android.repository.convert.asWeather
import dev.shuanghua.weather.data.android.repository.convert.asWeatherEntity
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val network: NetworkDataSource,
) {

    private suspend fun getNetworkWeather(params: String): Weather {
        return network.getMainWeather(params).asWeather()
    }

    /**
     * 首页定位城市调用 ( 保存数据库 )
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     * 插入数据库结束后，并不需要在当前函数返回，和 Ui 线程交互
     * 所以无需添加 withContext(io) 进行更细粒度的线程切换
     */
    suspend fun updateWeather(params: String) {
        val networkData: ShenZhenWeather = network.getMainWeather(params)
        weatherDao.insertWeather(
            weatherEntity = networkData.asWeatherEntity(),
            listAlarm = networkData.asAlarmEntityList(),
            listOneDay = networkData.asOneDayEntityList(),
            listOnHour = networkData.asOneHourEntityList(),
            listCondition = networkData.asConditionEntityList(),
            listExponent = networkData.asExponentEntityList()
        )
    }

    fun getOfflineWeather(): Flow<Weather> {
        val weatherResource = weatherDao.getWeather()
        return weatherResource.map {
            it?.asExternalModel() ?: emptyWeather
        }
    }

}