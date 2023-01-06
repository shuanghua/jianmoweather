package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.pojo.asExternalModel
import dev.shuanghua.weather.data.model.ShenZhenNetworkWeather
import dev.shuanghua.weather.data.model.WeatherResource
import dev.shuanghua.weather.data.model.asAlarmEntityList
import dev.shuanghua.weather.data.model.asOneDayEntityList
import dev.shuanghua.weather.data.model.asOneHourEntityList
import dev.shuanghua.weather.data.model.asWeatherEntity
import dev.shuanghua.weather.data.model.emptyWeatherResource
import dev.shuanghua.weather.data.model.mapToAutoLocationStationEntity
import dev.shuanghua.weather.data.model.mapToConditionEntityList
import dev.shuanghua.weather.data.model.mapToExponentEntityList
import dev.shuanghua.weather.shared.extensions.ifNullToEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * 遵循单一数据源
 * 从网络获取
 * 保存到数据库
 * 从数据库加载显示
 */
class OfflineWeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val stationDao: StationDao,
    private val network: SZNetworkDataSource,
) : WeatherRepository {

    override fun getWeather(): Flow<WeatherResource> {
        val packingWeatherFlow = weatherDao.getWeather()
        return packingWeatherFlow.map {
            it?.asExternalModel() ?: emptyWeatherResource
        }
    }

    /**
     * 首页定位城市调用(保存数据库)
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     */
    override suspend fun updateWeather(params: String) {
        val networkData: ShenZhenNetworkWeather? = network.getMainWeather(params)
        if (networkData != null) saveToDB(networkData)
    }

    /**
     * 将网络模型转成 App 的模型
     * 因为服务器数据的不确定性(比如有的集合对象为空地址，有的则为空内容), 所以:
     * 地址为空的 String 一律赋值 ""
     * 地址为空的 List 一律赋值 emptyList()
     */
    private suspend fun saveToDB(networkData: ShenZhenNetworkWeather) {
        val weatherEntity = networkData.asWeatherEntity()
        val alarmsIcons = networkData.asAlarmEntityList()// 预警图标
        val oneDays = networkData.asOneDayEntityList()
        val oneHours = networkData.asOneHourEntityList()// 每小时天气
        //val halfHours = networkData.asHalfHourEntityList() //半小时
        val conditions = networkData.mapToConditionEntityList()// 气压,湿度
        val healthExponents = networkData.mapToExponentEntityList()// 健康指数
        val locationStation = networkData.mapToAutoLocationStationEntity() //自动定位站点

        weatherDao.insertWeather(
            weatherEntity,
            alarmsIcons.ifNullToEmpty(),
            oneDays.ifNullToEmpty(),
            conditions.ifNullToEmpty(),
            oneHours.ifNullToEmpty(),
            healthExponents
        )
        if (networkData.autoObtid.isNotEmpty()) {
            stationDao.insertAutoStations(locationStation)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(
            weatherDao: WeatherDao,
            stationDao: StationDao,
            szNetworkDataSource: SZNetworkDataSource,
        ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OfflineWeatherRepository(
                    weatherDao,
                    stationDao,
                    szNetworkDataSource
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}