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
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.data.android.repository.convert.asOneDayEntityList
import dev.shuanghua.weather.data.android.repository.convert.asOneHourEntityList
import dev.shuanghua.weather.data.android.repository.convert.asWeatherEntity
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val network: NetworkDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {

    /**
     * 首页定位城市调用 ( 保存数据库 )
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     * 插入数据库结束后，并不需要在当前函数返回，和 Ui 线程交互
     * 所以无需添加 withContext(io) 进行更细粒度的线程切换
     */
    suspend fun updateWeather(params: String) =
        withContext(dispatchers.io) {
            val networkData: ShenZhenWeather = network.getMainWeather(params)
            weatherDao.insertWeather(
                weatherEntity = networkData.asExternalModel().asWeatherEntity(),
                listAlarm = networkData.asExternalModel().asAlarmEntityList(),
                listOneDay = networkData.asExternalModel().asOneDayEntityList(),
                listOnHour = networkData.asExternalModel().asOneHourEntityList(),
                listCondition = networkData.asExternalModel().asConditionEntityList(),
                listExponent = networkData.asExternalModel().asExponentEntityList()
            )
        }

    fun getOfflineWeather(): Flow<Weather> =
        weatherDao.getWeather().map {
            it?.asExternalModel() ?: emptyWeather
        }

}