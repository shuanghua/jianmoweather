package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.network.OuterParam
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
    private val dispatchers: AppCoroutineDispatchers,
) : UpdateUseCase<UpdateWeatherUseCase.Params>() {

    private var locationCityId: String = ""
    private var innerParam: WeatherParam = WeatherParam()
    private var fullRequestJson = ""

    private var _obtId = ""
    private var _isLocation = ""

    data class Params(val cityId: String, val cityName: String)

    /**
     * 获取定位城市
     * 查询城市id
     * 生成参数
     * 获取天气数据
     */
    override suspend fun doWork(params: Params) { // params = null 意味定位失败
        withContext(dispatchers.io) {
            // 定位， 更新定位数据
            //判断是否跨越城市
            //如还在当前城市， 判断上次选择的站点是否是“自动定位站点”  是，则使用包含经纬度的参数去请求，否则使用不好含经纬度的参数去请求
            //如果不在当前城市，直接使用包含经纬度的参数去请求

            //定位
            val location = locationRepository.getLocation() // 定位出错则抛异常
            // 更新定位数据
            val outerParam = OuterParam(
                pcity = location.cityName,
                parea = location.district,
                lon = location.longitude,
                lat = location.latitude
            )
            paramsRepository.updateOuterParam(outerParam)
            // 判断是否跨越城市
            // 如果跨越了城市,则获取当前城市ID

            // 笔记: 如果使用 !== , 当结构一样, 地址不一样,比如String a = “AB” ,String b = “AB”
            // a !== b 则返回 true ,所以 !== 是用来比较地址

            // 没有跨越城市，还在同一个城市
            val stationReturn = stationDao.queryStationReturn()
            if (stationReturn != null) {  // 假如用户上次退出前的首页是非自动定位站点 ，那么按上次的站点请求,isLocation = 0 ,也就是后面使用不包含经纬度的参数去请求
                _obtId = stationReturn.obtId
                _isLocation = stationReturn.isLocation   // “0和1都有可能” 上次站点是否是自动定位站点
            } else {     // 上次退出前没有选择任何站点，则按自动定位站点，也就是使用包含当前位置经纬度的参数请求，让服务器自己判断站点
                _obtId = ""
                _isLocation = "1"
            }

            //请求参数
            innerParam = WeatherParam(
                lon = location.longitude,
                lat = location.latitude,
                isauto = _isLocation, // 1
                cityids = if (_isLocation == "1") paramsRepository.cityIds else params.cityId, // 单独的站点请求：cityid 和 cityids 必须一致
                cityid = params.cityId,
                obtId = _obtId,      //自动定位不需要传入具体的站点id
                pcity = location.cityName,
                parea = location.district
            )
            fullRequestJson = paramsRepository.getWeatherJson(outerParam, innerParam)

            val a = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
            Timber.d("full- $a$fullRequestJson")

            weatherRepository.updateWeatherData(params = fullRequestJson)


//            val stationReturn = stationDao.queryStationReturn()//假如用户上次手动选择站点
//
//            if (stationReturn != null) {  //
//                _obtId = stationReturn.obtId
//                _isLocation = stationReturn.isLocation   // 上次站点是否是自动定位站点
//            } else {     // 没有手动选择站点，则使用默认定位的站点
//                _obtId = ""
//                _isLocation = "1"
//            }
//
//            if (_isLocation == "1") {  // 请求定位数据
//                val location = locationRepository.getLocation() // 定位出错则抛异常
//                // 更新外部参数
//                val outerParam = OuterParam(
//                    pcity = location.cityName,
//                    parea = location.district,
//                    lon = location.longitude,
//                    lat = location.latitude
//                )
//
//                paramsRepository.updateOuterParam(outerParam)
//
//                // 获取当前城市ID, 如果没有跨越城市,则跳过
//                if (params.cityName != location.cityName) { // 笔记: 此处如果使用 !== , 当结构一样, 地址不一样,会造成重复请求
//                    val cityName = location.cityName.substringBefore("市")
//                    val innerParam = QueryCityIdParam(cityName, paramsRepository.cityIds)
//                    val queryCityIdParam = paramsRepository.getCityIdJson(innerParam)
//                    locationCityId = cityRepository.requestCityIdByKeyWords(queryCityIdParam)
//                }
//
//                // 创建内部参数
//                innerParam = WeatherParam(
//                    lon = location.longitude,
//                    lat = location.latitude,
//                    isauto = _isLocation,//1
//                    cityids = paramsRepository.cityIds,
//                    cityid = params.cityId,
//                    obtId = "",//自动定位不需要传入具体的站点id
//                    pcity = location.cityName,
//                    parea = location.district
//                )
//                fullRequestJson = paramsRepository.getWeatherJson(outerParam, innerParam)
//            } else {
//                innerParam = WeatherParam(
//                    isauto = isLocation,//0  是否是定位，由站点页面的选择决定，默认=1
//                    cityid = params.cityId,//单独的站点请求：cityid 和 cityids 必须一致
//                    obtId = _obtId,
//                    cityids = params.cityId
//                )
//                fullRequestJson = paramsRepository.getWeatherJson(OuterParam(), innerParam)
//            }
//
////            val a = "http://szqxapp1.121.com.cn/phone/api/IndexV41.do?data="
////            Timber.d("full- $a$fullRequestJson")
//
//            weatherRepository.updateWeatherData(params = fullRequestJson)
        }
    }
}