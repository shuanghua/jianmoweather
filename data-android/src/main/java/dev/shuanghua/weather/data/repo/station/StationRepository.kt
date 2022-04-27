package dev.shuanghua.weather.data.repo.station

import dev.shuanghua.weather.data.model.ShenZhenCommon
import dev.shuanghua.weather.data.network.ShenZhenService
import dev.shuanghua.weather.data.model.StationReturn
import retrofit2.Response

class StationRepository(private val service: ShenZhenService) {

    suspend fun getStationList(param: String): Response<ShenZhenCommon<StationReturn>> {
        return service.getStationList(param)
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