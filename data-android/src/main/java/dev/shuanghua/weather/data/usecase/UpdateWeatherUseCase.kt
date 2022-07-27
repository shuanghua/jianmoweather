package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.network.CommonParam.isLocation
import dev.shuanghua.weather.data.network.ParentParam
import dev.shuanghua.weather.data.network.QueryCityIdParam
import dev.shuanghua.weather.data.network.WeatherParam
import dev.shuanghua.weather.data.repo.CityRepository
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
    private val cityRepository: CityRepository,//查询城市id
    private val weatherRepository: WeatherRepository,//请求天气
    private val stationDao: StationDao,
    private val dispatchers: AppCoroutineDispatchers
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

    private var locationCityId: String = ""
    private var childParam: WeatherParam = WeatherParam()
    private var fullRequestJson = ""

    private var _obtId = ""
    private var _isLocation = ""

    data class Params(
        val cityName: String,
        val cityId: String,
    )

    /**
     * 获取定位城市
     * 查询城市id
     * 生成参数
     * 获取天气数据
     */
    override suspend fun doWork(params: Params) { // params = null 意味定位失败
        withContext(dispatchers.io) {
            val stationReturn = stationDao.queryStationReturn()
            Timber.d("Update2:$stationReturn")

            if (stationReturn != null) {
                _obtId = stationReturn.obtId
                _isLocation = stationReturn.isLocation
            } else {
                _obtId = ""
                _isLocation = "1"
            }

            if (_isLocation == "1") {
                val location: LocationRepository.Location =
                    locationRepository.getLocation() // 定位出错则抛异常
                // 更新外部参数
                paramsRepository.parentParamWithLocation = ParentParam(
                    pcity = location.cityName,
                    parea = location.district,
                    lon = location.longitude,
                    lat = location.latitude
                )

                // 获取当前城市ID, 如果没有跨越城市,则跳过
                if (params.cityName != location.cityName) { // 笔记: 此处如果使用 !== , 当结构一样, 地址不一样,会造成重复请求
                    val cityName = location.cityName.substringBefore("市")
                    val innerParam = QueryCityIdParam(cityName, paramsRepository.cityIds)
                    val queryCityIdParam = paramsRepository.getCityIdJson(innerParam)
                    locationCityId = cityRepository.requestCityIdByKeyWords(queryCityIdParam)
                }

                // 创建请求参数的内部参数
                childParam = WeatherParam(
                    lon = location.longitude,
                    lat = location.latitude,
                    isauto = _isLocation,//1
                    cityids = paramsRepository.cityIds,
                    cityid = params.cityId,
                    obtId = "",//自动定位不需要传入具体的站点id
                    pcity = location.cityName,
                    parea = location.district
                )
                fullRequestJson =
                    paramsRepository.getWeatherJson(
                        paramsRepository.parentParamWithLocation,
                        childParam
                    )
            } else {
                childParam = WeatherParam(
                    isauto = isLocation,//0  是否是定位，由站点页面的选择决定
                    cityid = params.cityId,//单独的站点请求：cityid 和 cityids 必须一致
                    obtId = _obtId,
                    cityids = params.cityId
                )
                fullRequestJson =
                    paramsRepository.getWeatherJson(paramsRepository.parentParam, childParam)
            }

//            val a = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
//            Timber.d("full- $a$fullRequestJson")

            weatherRepository.updateWeatherData(params = fullRequestJson)
        }
    }
}