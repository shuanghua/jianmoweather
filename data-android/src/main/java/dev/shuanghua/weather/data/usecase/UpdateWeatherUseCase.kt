package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.asExternalModel
import dev.shuanghua.weather.data.model.SelectedStation
import dev.shuanghua.weather.data.network.InnerParam
import dev.shuanghua.weather.data.network.asMainWeatherParam
import dev.shuanghua.weather.data.network.asOuterParam
import dev.shuanghua.weather.data.repo.LocationRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.WeatherRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * 首页天气数据获取（包含定位），网络请求->保存数据库
 */
class UpdateWeatherUseCase @Inject constructor(
    private val locationRepository: LocationRepository,//定位
    private val paramsRepository: ParamsRepository,//网络请求参数
    private val weatherRepository: WeatherRepository,//请求天气
    private val stationDao: StationDao,
    private val dispatchers: AppCoroutineDispatchers,
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

    data class Params(val cityId: String)

    //首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
    private val defaultLocationStation = SelectedStation(obtId = "", isLocation = "1")

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {

            // 定位
            val networkLocationDeferred = async { locationRepository.getNetworkLocation() }
            val offlineLocationDeferred = async { locationRepository.getOfflineLocation() }
            val stationDeferred = async { stationDao.querySelectedStation() }
            val networkLocation = networkLocationDeferred.await()
            val offlineLocation = offlineLocationDeferred.await()
            val lastStation = stationDeferred.await()

            // 站点
            val station =
                if (offlineLocation.cityName != networkLocation.cityName) {
                    defaultLocationStation
                } else {
                    lastStation?.asExternalModel() ?: defaultLocationStation
                }

            // 参数
            val innerParam = InnerParam(
                lon = networkLocation.longitude,
                lat = networkLocation.latitude,
                isauto = station.isLocation,
                cityids = paramsRepository.cityIds,
                cityid = params.cityId,
                obtId = station.obtId,      //自动定位不需要传入具体的站点id
                pcity = networkLocation.cityName,
                parea = networkLocation.district
            )

            val json = paramsRepository.createMainWeatherRequestJson(
                innerParam.asOuterParam(),
                innerParam.asMainWeatherParam()
            )

            val url = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
            Timber.d("首页天气:$url$json")

            // 请求
            launch { weatherRepository.updateWeather(json) }
            launch { locationRepository.setOfflineLocation(networkLocation) }
            paramsRepository.updateInnerParam(innerParam)
        }
    }
}

// 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
// a !== b 则返回 true ,所以 !== 是用来比较地址