package dev.shuanghua.weather.data.repo.weather

import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.data.model.MainReturn
import dev.shuanghua.weather.data.network.ShenZhenService
import dev.shuanghua.weather.shared.extensions.ifNullToEmpty
import dev.shuanghua.weather.shared.extensions.ifNullToValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val service: ShenZhenService
) {
    //private val repoListRateLimit = RateLimiter<String>(1000, TimeUnit.MILLISECONDS) // 1秒测试
    //if (repoListRateLimit.shouldFetch(requestParams.cityId)) {} // 如果数据库没有数据或者数据过期


    /**
     * 收藏城市调用(暂不设计将其保存到数据库)
     */
    fun getWeatherData(screen: String, params: String): Flow<JianMoWeatherModel?> = flow {
        //data 为 null 只能是服务器没有数据(比如API过期)
        val remoteData = service.getWeather(params).body()?.data
        if (remoteData != null) {
            emit(handleSZData(screen, remoteData.cityid!!, remoteData))
        } else {
            throw Exception("服务器数据为:Null")
        }
    }

    /**
     * 首页定位城市调用(保存数据库)
     * 如果以后保存数据库，则需要 screen + cityId 两个条件查询
     * 由数据库自动识别数据变动来触发订阅回调，所以不需要返回值
     */
    suspend fun updateWeatherData(screen: String, params: String) {
        val remoteData = service.getWeather(params).body()?.data ?: return
        val weatherData = handleSZData(screen, remoteData.cityid!!, remoteData)
        weatherData.apply {
            saveWeatherData(
                temperature, alarms, oneDays, oneHours, conditions, healthExponents
            )
        }
    }

    /**
     * 将网络模型转成 App 的模型
     * 因为服务器数据的不确定性(比如有的集合对象为空地址，有的则为空内容), 所以:
     * 地址为空的 String 一律赋值 ""
     * 地址为空的 List 一律赋值 emptyList()
     */
    private fun handleSZData(screen: String, cityId: String, data: MainReturn) =
        data.run {
            var sunUp = halfCircle?.sunup
            var sunDown = halfCircle?.sundown
            sunUp?.let {
                if (it[0] != '0') {//10点到晚上11：59
                    sunUp = sunDown.also { sunDown = sunUp }
                }
            }

            //val iconName = dayList?.firstOrNull()?.weatherpic.ifNullToValue()

            val id = cityid.ifNullToValue(cityId)
            val lunarCalendar: String? = lunar?.run { "$info1 $info2 $info3 $info4 $info5" }
            val airQuality: String = aqi?.aqi.ifNullToValue() + "·" + aqi?.aqic.ifNullToValue()

            val temperature = Temperature(
                screen = screen,
                cityId = cityId,
                cityName = cityName.ifNullToValue(),
                temperature = t.ifNullToValue(),
                description = dayList?.get(0)?.desc1.ifNullToValue(),
                aqi = airQuality.ifNullToValue(),
                lunar = lunarCalendar.ifNullToValue(),
                stationName = stationName.ifNullToValue()
            )

            // Alarm :
            var alarmsIconUrl = ""
            val alarms = alarmList?.mapIndexed { index, alarm ->
                if (alarm.icon != null && alarm.icon != "") {
                    alarmsIconUrl = ShenZhenService.ICON_HOST + alarm.icon
                }
                Alarm(
                    id = index,
                    cityId = id,
                    icon = alarmsIconUrl,
                    name = alarm.name.ifNullToValue()
                )
            }

            // OneDay: 虽然每次插入都替换,触发自动查询,但只要数据结构一样就行, 这对 Compose 很重要
            val oneDays = dayList?.mapIndexed { index, oneDay ->
                OneDay(
                    id = index,
                    cityId = id,
                    date = oneDay.date.ifNullToValue(),
                    week = oneDay.week.ifNullToValue(),
                    desc = oneDay.desc.ifNullToValue(),
                    t = "${oneDay.minT}~${oneDay.maxT}",
                    minT = oneDay.minT.ifNullToValue(),
                    maxT = oneDay.maxT.ifNullToValue(),
                    iconName = oneDay.wtype.ifNullToValue()
                )
            }

            // OndeHour
            val oneHours = hourForeList?.mapIndexed { index, oneHour ->
                OneHour(
                    id = index,
                    cityId = id,
                    hour = oneHour.hour.ifNullToValue(),
                    t = oneHour.t.ifNullToValue(),
                    icon = oneHour.weatherpic.ifNullToValue(),
                    rain = oneHour.rain.ifNullToValue()
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

            // 气压,湿度
            val conditions = ArrayList<Condition>()
            if (!rh.isNullOrEmpty()) {
                val rhItem = Condition(
                    id = 0,
                    cityId = cityId,
                    name = "湿度",
                    value = rh
                )
                conditions.add(rhItem)
            }
            if (!pa.isNullOrEmpty()) {
                val hPaItem = Condition(
                    id = 1,
                    cityId = cityId,
                    name = "气压",
                    value = pa
                )
                conditions.add(hPaItem)
            }
            if (!wwCN.isNullOrEmpty()) {//东风
                val windItem =
                    Condition(
                        id = 2,
                        cityId = cityId,
                        name = wwCN,
                        value = wf.ifNullToValue()
                    )
                conditions.add(windItem)
            }
            if (!r24h.isNullOrEmpty()) {
                val r24hItem = Condition(
                    id = 3,
                    cityId = cityId,
                    name = "24H降雨量",
                    value = r24h
                )
                conditions.add(r24hItem)
            }
            if (!r01h.isNullOrEmpty()) {
                val r01hItem = Condition(
                    id = 4,
                    cityId = cityId,
                    name = "1H降雨量",
                    value = r01h
                )
                conditions.add(r01hItem)
            }
            if (!v.isNullOrEmpty()) {
                val visibilityItem = Condition(
                    id = 5,
                    cityId = cityId,
                    name = "能见度",
                    value = v
                )
                conditions.add(visibilityItem)
            }

            // 健康指数
            val healthExponents = ArrayList<Exponent>()
            jkzs?.apply {
                shushidu?.apply {
                    val healthExponent = Exponent(
                        id = 0,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                gaowen?.apply {
                    val healthExponent = Exponent(
                        id = 1,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                ziwaixian?.apply {
                    val healthExponent = Exponent(
                        id = 2,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                co?.apply {
                    val healthExponent = Exponent(
                        id = 3,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                meibian?.apply {
                    val healthExponent = Exponent(
                        id = 4,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                chenlian?.apply {
                    val healthExponent = Exponent(
                        id = 5,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                luyou?.apply {
                    val healthExponent = Exponent(
                        id = 6,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                liugan?.apply {
                    val healthExponent = Exponent(
                        id = 7,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(healthExponent)
                }
                chuanyi?.apply {
                    val exponent = Exponent(
                        id = 8,
                        cityId = cityId,
                        title = title.ifNullToValue(),
                        level = level.ifNullToValue(),
                        levelDesc = level_desc.ifNullToValue(),
                        levelAdvice = level_advice.ifNullToValue()
                    )
                    healthExponents.add(exponent)
                }
            }

            return@run JianMoWeatherModel(
                temperature = temperature,
                alarms = alarms.ifNullToEmpty(), // map
                oneDays = oneDays.ifNullToEmpty(), // map
                oneHours = oneHours.ifNullToEmpty(), // map
                conditions = conditions, // new ArrayList
                healthExponents = healthExponents // new ArrayList
            )
        }

    private suspend fun saveWeatherData(
        temperature: Temperature,
        alarms: List<Alarm>,
        oneDays: List<OneDay>,
        oneHours: List<OneHour>,
        conditions: List<Condition>,
        exponents: List<Exponent>
    ) {
        weatherDao.insertWeather(temperature, alarms, oneDays, conditions, oneHours, exponents)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(
            weatherDao: WeatherDao,
            service: ShenZhenService
        ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WeatherRepository(
                    weatherDao,
                    service
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}

data class JianMoWeatherModel(
    val temperature: Temperature,
    val alarms: List<Alarm> = emptyList(),
    val oneDays: List<OneDay> = emptyList(),
    val oneHours: List<OneHour> = emptyList(),
    val conditions: List<Condition> = emptyList(),
    val healthExponents: List<Exponent> = emptyList()
)