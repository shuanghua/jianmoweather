package jianmoweather.data.repo.weather

import com.moshuanghua.jianmoweather.shared.extensions.ifNullToEmpty
import com.moshuanghua.jianmoweather.shared.extensions.ifNullToValue
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.*
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.network.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class WeatherRepository(
    private val weatherDao: WeatherDao, private val remoteDataSource: WeatherRemoteDataSource
) {
    //private val repoListRateLimit = RateLimiter<String>(1000, TimeUnit.MILLISECONDS) // 1秒测试
    //if (repoListRateLimit.shouldFetch(requestParams.cityId)) {} // 如果数据库没有数据或者数据过期


    /**
     * 收藏城市调用(暂不设计将其保存到数据库)
     */
    fun getWeatherData(screen: String, params: Params): Flow<JianMoWeatherModel?> = flow {
        //data 为 null 只能是服务器没有数据(比如API过期)
        val remoteData = remoteDataSource.requestWeather(params)
        if(remoteData != null) {
            emit(handleSZData(screen, params.cityId, remoteData))
        } else {
            throw Exception("服务器数据为:Null")
        }
    }

    /**
     * 首页定位城市调用(保存数据库)
     * 如果以后保存数据库，则需要 screen + cityId 两个条件查询
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     */
    suspend fun updateWeatherData(screen: String, params: Params) {
        val remoteData = remoteDataSource.requestWeather(params) ?: return
        val weatherData = handleSZData(screen, params.cityId, remoteData)
        weatherData.apply {
            saveWeatherData(
                temperature, alarms, oneDays, oneHours, otherItems, healthExponents
            )
        }
    }

    /**
     * 将网络模型转成 App 的模型
     * 因为服务器数据的不确定性(比如有的集合对象为空地址，有的则为空内容), 所以:
     * 地址为空的 String 一律赋值 ""
     * 地址为空的 List 一律赋值 emptyList()
     */
    private fun handleSZData(screen: String, cityId: String, data: WeatherData) =
        data.run {
            var sunUp = halfCircle?.sunup
            var sunDown = halfCircle?.sundown
            sunUp?.let {
                if(it[0] != '0') {//10点到晚上11：59
                    sunUp = sunDown.also { sunDown = sunUp }
                }
            }

            //val iconName = dayList?.firstOrNull()?.weatherpic.ifNullToValue()

            val id = cityid.ifNullToValue(cityId)
            val lunarCalendar: String? = lunar?.run { "$info1 $info2 $info3 $info4 $info5" }
            val airQuality: String = aqi?.aqi.ifNullToValue() + "·" + aqi?.aqic.ifNullToValue()

            val temperature = WeatherScreenEntity(
                screen = screen,
                cityId = cityId,
                cityName = cityName.ifNullToValue(),
                temperature = t.ifNullToValue(),
                description = dayList?.get(0)?.desc1.ifNullToValue(),
                aqi = airQuality.ifNullToValue(),
                lunar = lunarCalendar.ifNullToValue(),
                stationName = stationName.ifNullToValue()
            )

            var alarmsIconUrl = ""
            val alarms = alarmList?.map {
                if(it.icon != null && it.icon != "") {
                    alarmsIconUrl = ShenZhenService.ICON_HOST + it.icon
                }

                Alarm(
                    cityId = id, icon = alarmsIconUrl, name = it.name.ifNullToValue()
                )
            }

            val oneDays = dayList?.map {
                OneDay(
                    cityId = id,
                    date = it.date.ifNullToValue(),
                    week = it.week.ifNullToValue(),
                    desc = it.desc.ifNullToValue(),
                    minT = it.minT.ifNullToValue(),
                    maxT = it.maxT.ifNullToValue(),
                    iconName = it.wtype.ifNullToValue()
                )
            }

            val oneHours = hourForeList?.map {
                OneHour(
                    cityId = id,
                    hour = it.hour.ifNullToValue(),
                    t = it.t.ifNullToValue(),
                    icon = it.weatherpic.ifNullToValue(),
                    rain = it.rain.ifNullToValue()
                )
            }

//            val halfHours = halfCircle?.halfCircleList?.map {
//                HalfHour(
//                    cityId = id,
//                    hour = it.hour.ifNullToValue(),
//                    t = it.t.ifNullToValue()
//                )
//            }
//            halfHours?.let { weatherDao.insertHalfHours(it) }


            val otherItems = ArrayList<OtherItem>()
            if(!rh.isNullOrEmpty()) {
                val rhItem = OtherItem(cityId = cityId, name = "湿度", value = rh)
                otherItems.add(rhItem)
            }
            if(!pa.isNullOrEmpty()) {
                val hPaItem = OtherItem(cityId = cityId, name = "气压", value = pa)
                otherItems.add(hPaItem)
            }
            if(!wwCN.isNullOrEmpty()) {//东风
                val windItem = OtherItem(cityId = cityId, name = wwCN, value = wf.ifNullToValue())
                otherItems.add(windItem)
            }
            if(!r24h.isNullOrEmpty()) {
                val r24hItem = OtherItem(cityId = cityId, name = "24H降雨量", value = r24h)
                otherItems.add(r24hItem)
            }
            if(!r01h.isNullOrEmpty()) {
                val r01hItem = OtherItem(cityId = cityId, name = "1H降雨量", value = r01h)
                otherItems.add(r01hItem)
            }
            if(!v.isNullOrEmpty()) {
                val visibilityItem = OtherItem(cityId = cityId, name = "能见度", value = v)
                otherItems.add(visibilityItem)
            }

            val healthExponents = ArrayList<HealthExponent>()
            jkzs?.apply {
                shushidu?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                gaowen?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                ziwaixian?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                co?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                meibian?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                chenlian?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                luyou?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                liugan?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }


                chuanyi?.apply {
                    val healthExponent = HealthExponent(
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = "[${level_desc.ifNullToValue()}]",
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
            }

            return@run JianMoWeatherModel(
                temperature = temperature,
                alarms = alarms.ifNullToEmpty(),
                oneDays = oneDays.ifNullToEmpty(),
                oneHours = oneHours.ifNullToEmpty(),
                otherItems = otherItems.ifNullToEmpty(),
                healthExponents = healthExponents.ifNullToEmpty()
            )
        }

    private suspend fun saveWeatherData(
        temperature: WeatherScreenEntity,
        alarms: List<Alarm>,
        oneDays: List<OneDay>,
        oneHours: List<OneHour>,
        otherItems: List<OtherItem>,
        healthExponents: List<HealthExponent>
    ) {
        Timber.d("网络数据插入----------------------")
        weatherDao.insertTemperature(temperature)
        weatherDao.insertAlarms(alarms)
        weatherDao.insertOneDays(oneDays)
        weatherDao.insertOtherItems(otherItems)
        weatherDao.insertOneHours(oneHours)
        weatherDao.insertHealthExponent(healthExponents)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(
            weatherDao: WeatherDao, remoteDataSource: WeatherRemoteDataSource
        ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WeatherRepository(
                    weatherDao, remoteDataSource
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}

data class JianMoWeatherModel(
    val temperature: WeatherScreenEntity,
    val alarms: List<Alarm> = emptyList(),
    val oneDays: List<OneDay> = emptyList(),
    val oneHours: List<OneHour> = emptyList(),
    val otherItems: List<OtherItem> = emptyList(),
    val healthExponents: List<HealthExponent> = emptyList()
)