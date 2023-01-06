package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.StationEntity

class StationRepository(private val stationDao: StationDao) {
    suspend fun saveStations(stations: List<StationEntity>) {
        stationDao.insertStations(stations)
    }

    companion object {
        @Volatile
        private var INSTANCE: StationRepository? = null

        fun getInstance(
            stationDao: StationDao
        ): StationRepository {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: StationRepository(stationDao).also { INSTANCE = it }
                }
        }
    }
}