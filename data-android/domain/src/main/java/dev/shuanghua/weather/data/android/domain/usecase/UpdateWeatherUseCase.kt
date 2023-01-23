package dev.shuanghua.weather.data.android.domain.usecase


import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * 首页天气数据获取（包含定位），网络请求->保存数据库
 */
class UpdateWeatherUseCase @Inject constructor(
    private val locationRepository: LocationRepository, // 定位
    private val paramsRepository: ParamsRepository, // 转换参数
    private val weatherRepository: WeatherRepository, // 获取天气
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

    // 首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
    data class Params(val cityId: String, val lastStation: SelectedStation)

    override suspend fun doWork(params: Params): Unit = withContext(dispatchers.io) {

        // 定位 并行
        val networkLocationDeferred = async { locationRepository.getNetworkLocation() }
        val offlineLocationDeferred = async { locationRepository.getLocationFromDataStore() }

        val networkLocation = networkLocationDeferred.await() // 当前定位
        val offlineLocation = offlineLocationDeferred.await() // 上一次定位

        // 站点
        val station =
            if (offlineLocation.cityName != networkLocation.cityName) {
                SelectedStation("", "1")
            } else {
                params.lastStation
            }

        // 首次安装 cityId 为 "" 时，需提供一个默认城市 id，这样才能根据定位的经纬度信息请求所在城市天气
        val cityId = params.cityId.ifBlank { "28060159493" }

        // 参数
        val weatherParams = WeatherParams(
            lon = networkLocation.longitude,
            lat = networkLocation.latitude,
            isAuto = station.isLocation,
            cityIds = cityId, // 首页的请求只要确保 ids 不为空即可
            cityId = cityId,    // 首次安装提供一个默认城市 id，之后的ID都是从天气返回中的 cityId 获取
            obtId = station.obtId,      //自动定位不需要传入具体的站点id
            cityName = networkLocation.cityName,
            district = networkLocation.district
        ).also { paramsRepository.setWeatherParams(it) }

        val json = paramsRepository.weatherParamsToJson(weatherParams)

        // 请求
        launch { weatherRepository.updateWeather(json) }
        launch { locationRepository.saveLocationToDataStore(networkLocation) }
    }

}


// 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
// a !== b 则返回 true ,所以 !== 是用来比较地址