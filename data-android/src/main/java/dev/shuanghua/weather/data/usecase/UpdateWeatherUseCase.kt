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
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * (首页)定位城市数据获取
 * 定位 + 查询城市id + 获取天气数据
 */
class UpdateWeatherUseCase @Inject constructor(
    private val locationRepository: LocationRepository,//定位
    private val paramsRepository: ParamsRepository,//网络请求参数
    private val weatherRepository: WeatherRepository,//请求天气
    private val stationDao: StationDao,
    private val dispatchers: AppCoroutineDispatchers,
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

    private var innerParam = InnerParam()
    private var requestJson = ""

    data class Params(val cityId: String)

    /**
     * 获取定位城市
     * 查询城市id
     * 生成参数
     * 获取天气数据
     */
    override suspend fun doWork(params: Params) { // params = null 意味定位失败
//      首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
        withContext(dispatchers.io) {
            val cityId = params.cityId

            // 定位， 更新定位数据
            //判断是否跨越城市
            //如还在当前城市， 判断上次选择的站点是否是“自动定位站点”  是，则使用包含经纬度的参数去请求，否则使用不好含经纬度的参数去请求
            //如果不在当前城市，直接使用包含经纬度的参数去请求

            //定位

            val networkLocation = locationRepository.getNetworkLocation() // 定位出错则抛异常
            Timber.e("network:$networkLocation")

            val offlineLocation = locationRepository.getOfflineLocation() // 同步调用
            Timber.e("offline:$offlineLocation")



            val station = if (offlineLocation.cityName != networkLocation.cityName) {
                SelectedStation(obtId = "", isLocation = "1")
            } else {
                stationDao.querySelectedStation()
                    ?.asExternalModel()
                    ?: SelectedStation(obtId = "", isLocation = "1")
            }
            Timber.e("station:$station")


            locationRepository.setOfflineLocation(networkLocation)

            //请求参数
            innerParam = InnerParam(
                lon = networkLocation.longitude,
                lat = networkLocation.latitude,
                isauto = station.isLocation,
                cityids = paramsRepository.cityIds,
                cityid = cityId,
                obtId = station.obtId,      //自动定位不需要传入具体的站点id
                pcity = networkLocation.cityName,
                parea = networkLocation.district
            )
            paramsRepository.updateInnerParam(innerParam) //更新当前位置数据

            requestJson = paramsRepository.createMainWeatherRequestJson(
                innerParam.asOuterParam(),
                innerParam.asMainWeatherParam()
            )

            val a = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
            Timber.d("----->>Full $a$requestJson")

            weatherRepository.updateWeather(params = requestJson)
        }
        // 更新定位数据
        // 判断是否跨越城市
        // 如果跨越了城市,则获取当前城市ID,并把站点类型重置为定位类型
        // 没有跨越城市，还在同一个城市，使用上次退出前选择的站点
    }

}

// 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
// a !== b 则返回 true ,所以 !== 是用来比较地址