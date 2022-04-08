package jianmoweather.data.repo.station

import com.moshuanghua.jianmoweather.shared.util.toJsonString
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.network.ShenZhen
import jianmoweather.data.network.StationList
import jianmoweather.data.network.outerRequestParamsMap
import retrofit2.Response

class StationRepository(private val service: ShenZhenService) {

    suspend fun getStationAsync(
        cityId: String,
        cityName: String,
        district: String,
        lat: String,
        lon: String
    ): Response<ShenZhen<StationList>> {
        val paramMap = buildShenZhenStationParamsMap(cityId, lat, lon)


        val requestMap = outerRequestParamsMap(
            paramMap, cityName, district, lat, lon
        )


        val url = requestMap.toJsonString()
        return service.getStationAsync(url)
    }

    private fun buildShenZhenStationParamsMap(
        cityId: String,
        lat: String,
        lon: String
    ): Map<String, String> {
        return mapOf("cityid" to cityId, "lat" to lat, "lon" to lon)
    }


    companion object {
        @Volatile
        private var INSTANCE: StationRepository? = null

        fun getInstance(
            service: ShenZhenService
        ): StationRepository {
            return INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: StationRepository(service).also { INSTANCE = it }
            }
        }
    }
}