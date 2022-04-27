package dev.shuanghua.weather.data.repo.weather

import dev.shuanghua.weather.data.model.MainReturn
import dev.shuanghua.weather.data.network.ShenZhenService

class WeatherRemoteDataSource(private val api: ShenZhenService) {

    suspend fun requestWeather(requestParam: String): MainReturn? {
        return api.getWeather(requestParam).body()?.data!!
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSource? = null


        // 内
        val cityid: String = "28060159493"

        val isauto: String = "1"

        val w: String = "1080"
        val h: String = "2215"

        val cityids: String =
            "28060159493,32010145005,28010159287,02010058362,01010054511,30120659033"

        val pcity: String = "深圳市"
        val parea: String = "宝安区"

        val lon: String = "113.81035121710433"
        val lat: String = "22.760361451345034"

        val gif: String = "true"


        fun getInstance(service: ShenZhenService): WeatherRemoteDataSource {
            return INSTANCE ?: synchronized(this) { // Double single instance
                INSTANCE ?: WeatherRemoteDataSource(service).also { INSTANCE = it }
            }
        }
    }
}