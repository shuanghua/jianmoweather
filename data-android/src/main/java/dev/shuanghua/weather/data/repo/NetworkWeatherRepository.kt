package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.pojo.PackingWeather
import dev.shuanghua.weather.data.db.pojo.asExternalModel
import dev.shuanghua.weather.data.model.WeatherResource
import dev.shuanghua.weather.data.model.asAlarmEntityList
import dev.shuanghua.weather.data.model.asOneDayEntityList
import dev.shuanghua.weather.data.model.asOneHourEntityList
import dev.shuanghua.weather.data.model.asWeatherEntity
import dev.shuanghua.weather.data.model.mapToConditionEntityList
import dev.shuanghua.weather.data.model.mapToExponentEntityList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkWeatherRepository @Inject constructor(
    private val network: SZNetworkDataSource,
) {
    suspend fun getWeather(params: String): Flow<WeatherResource> = flow {
        val networkData = network.getMainWeather(params)
        if (networkData != null) {
            val weatherEntity = networkData.asWeatherEntity()
            val alarmsIcons = networkData.asAlarmEntityList()// 预警图标
            val oneDays = networkData.asOneDayEntityList()
            val oneHours = networkData.asOneHourEntityList()// 每小时天气
            //val halfHours = networkData.asHalfHourEntityList() //半小时
            val conditions = networkData.mapToConditionEntityList()// 气压,湿度
            val healthExponents = networkData.mapToExponentEntityList()// 健康指数
            //val locationStation = networkData.mapToAutoLocationStationEntity() //自动定位站点
            emit(
                PackingWeather(
                    weatherEntity = weatherEntity,
                    alarmIcons = alarmsIcons,
                    oneDays = oneDays,
                    conditions = conditions,
                    oneHours = oneHours,
                    exponents = healthExponents
                ).asExternalModel()
            )
        }
    }
}