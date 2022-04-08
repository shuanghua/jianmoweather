package jianmoweather.data.repo.weather

import com.moshuanghua.jianmoweather.shared.util.toJsonString
import jianmoweather.data.db.entity.Params
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.network.WeatherData
import jianmoweather.data.network.outerRequestParamsMap
import timber.log.Timber

class WeatherRemoteDataSource(private val api: ShenZhenService) {

    suspend fun requestWeather(
        params: Params
    ): WeatherData? {
        val innerParamsMap = innerRequestParamsMap(
            params.cityId,
            params.stationId,
            params.ids,
            params.isLocation
        )
        val outerParamMap = outerRequestParamsMap(
            innerParamsMap,
            params.cityName,
            params.district,
            params.latitude,
            params.longitude
        )

        val requestParam = outerParamMap.toJsonString()
        Timber.d("请求 Weather 参数： $requestParam")
        return api.getWeather(requestParam).body()?.data!!
    }

    private fun innerRequestParamsMap(
        cityId: String,
        stationId: String,
        ids: String,
        isAutoLocation: String
    ): Map<String, String> {
        return mapOf(
            "cityid" to cityId,
            "cityids" to ids,
            "isauto" to isAutoLocation,
            "obtid" to stationId,
            "h" to H,
            "w" to W
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSource? = null

        const val UID = "860505026439122sztq"
        const val OS = "android19"
        const val VER = "v5.1.4"
        const val NET = "WIFI" //4G
        const val TYPE = "1" //4G
        const val TOKEN = "0669bfe7df2b3d2044ff8c23860b698a2ec1bdf5"
        const val REVER = "514"
        const val UNAME = ""
        const val H = "720"
        const val W = "1184"

        fun getInstance(service: ShenZhenService): WeatherRemoteDataSource {
            return INSTANCE ?: synchronized(this) { // Double single instance
                INSTANCE ?: WeatherRemoteDataSource(service).also { INSTANCE = it }
            }
        }
    }
}