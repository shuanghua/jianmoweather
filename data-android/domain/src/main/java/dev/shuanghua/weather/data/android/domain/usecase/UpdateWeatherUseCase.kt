package dev.shuanghua.weather.data.android.domain.usecase


import dev.shuanghua.weather.data.android.model.MainWeatherParams
import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.asInnerParams
import dev.shuanghua.weather.data.android.model.asOuterParams
import dev.shuanghua.weather.data.android.model.request.WeatherScreenRequest
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

    //首页定位请求参数的 isauto = 1 切换站点时也一样， 收藏页面城市请求参数 isauto = 0
    data class Params(val cityId: String, val lastStation: SelectedStation)

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {

        // 定位
        val networkLocationDeferred = async { locationRepository.getNetworkLocation() }
        val offlineLocationDeferred = async { locationRepository.getOfflineLocation() }

        val networkLocation = networkLocationDeferred.await()
        val offlineLocation = offlineLocationDeferred.await()

        // 站点
        val station =
            if (offlineLocation.cityName != networkLocation.cityName) {
                SelectedStation("", "1")
            } else {
                params.lastStation
            }

        // 首次安装 cityId 为 ""
        val cityId = if (params.cityId.isNotBlank()) params.cityId else "28060159493"

        // 参数
        val mainWeatherParams = MainWeatherParams(
            lon = networkLocation.longitude,
            lat = networkLocation.latitude,
            isauto = station.isLocation,
            cityids = cityId, // 首页的请求只要确保 ids 不为空即可
            cityid = cityId,
            obtId = station.obtId,      //自动定位不需要传入具体的站点id
            pcity = networkLocation.cityName,
            parea = networkLocation.district
        )

        val json = paramsRepository.getMainWeatherRequestParams(
            WeatherScreenRequest(
                innerParams = mainWeatherParams,
                outerParams = mainWeatherParams.asOuterParams()
            )
        )

        val url = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
        Timber.d("首页天气:$url$json")

        // 请求
        launch { weatherRepository.updateWeather(json) }
        launch { locationRepository.setOfflineLocation(networkLocation) }
        paramsRepository.updateRequestParams(
            ParamsRepository.Params(
                mainWeatherParams.asInnerParams(),
                mainWeatherParams.asOuterParams()
            )
        )
    }
}


// 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
// a !== b 则返回 true ,所以 !== 是用来比较地址