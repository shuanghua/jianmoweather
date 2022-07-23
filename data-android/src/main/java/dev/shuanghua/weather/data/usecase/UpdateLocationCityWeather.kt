package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.UpdateUseCase
import dev.shuanghua.weather.data.db.entity.WeatherParam
import dev.shuanghua.weather.data.repo.city.CityRepository
import dev.shuanghua.weather.data.repo.location.LocationRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.weather.WeatherRepository
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * (首页)定位城市数据获取
 * 定位 + 查询城市id + 获取天气数据
 */
class UpdateLocationCityWeather @Inject constructor(
    private val locationRepository: LocationRepository,
    private val paramsRepository: ParamsRepository,
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateLocationCityWeather.Params>() {

    private val lastLocationCity: String = ""
    private var locationCityId: String = ""

    data class Params(val screen: String)

    /**
     * 获取定位城市
     * 查询城市id
     * 生成参数
     * 获取天气数据
     */
    override suspend fun doWork(params: Params) { // params = null 意味定位失败
        withContext(dispatchers.io) {
            // 此处 requestParams 永远不为空, 新的请求参数  缓存的请求参数  默认参数
            try {
                // 定位
                val location: LocationRepository.Location = locationRepository.getLocation() // 定位出错则抛异常
                Timber.d("-------------------->>$location")

                // 更新外部参数
                paramsRepository.updateOuterParam(
                    cityName = location.cityName,
                    district = location.district,
                    longitude = location.longitude,
                    latitude = location.latitude
                )

                // 获取当前城市ID, 如果没有跨越城市,则跳过
                if (lastLocationCity != location.cityName) { // 笔记: 此处如果使用 !== , 当结构一样, 地址不一样,会造成重复请求
                    val cityIdRequestParam =
                        paramsRepository.getCityIdByKeyWordsRequestJson(location.cityName)
                    locationCityId = cityRepository.requestCityIdByKeyWords(cityIdRequestParam)
                }


                // 创建请求参数的内部参数
                val innerParam = WeatherParam(
                    lon = location.longitude,
                    lat = location.latitude,
                    isauto = "1",
                    cityids = paramsRepository.cityIds,
                    cityid = locationCityId,
                    pcity = location.cityName,
                    parea = location.district
                )

                // 生成完整请求参数( 针对当前定位位置 )
                val fullParam = paramsRepository.getLocationCityWeatherRequestJson(innerParam)

                // 网络更新数据
                weatherRepository.updateWeatherData(params.screen, fullParam)

            } catch (t: Throwable) {
                Timber.d("-------------------->>$t")

                // 如果只是单纯的定位失败, -> 继续更新天气数据
                // 如果是在请求天气数据过程中失败,例如网络错误, -> 显示旧数据
                val requestParams = paramsRepository.getLastLocationCityWeatherParam()
                weatherRepository.updateWeatherData(params.screen, requestParams)
                throw t
            }
        }
    }
}