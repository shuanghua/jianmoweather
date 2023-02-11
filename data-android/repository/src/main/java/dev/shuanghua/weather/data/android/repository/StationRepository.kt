package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.StationDao
import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.Station
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.data.android.repository.converter.asWeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StationRepository(private val stationDao: StationDao) {

    suspend fun saveStationList(stations: List<Station>) {
        stationDao.insertStations(stations.map(Station::asWeatherEntity))
    }

    fun observerStationList(districtName: String): Flow<List<Station>> {
        return stationDao.observerStations(districtName).map {
            it.map(StationEntity::asExternalModel)
        }
    }

    suspend fun saveSelectedStation(selectedStation: SelectedStation) {
        stationDao.insertSelectedStation(selectedStation.asWeatherEntity())
    }

    fun getSelectedStation(): Flow<SelectedStation?> = stationDao.getLastStation()
        .map {
            it?.asExternalModel()
        }

    companion object {
        @Volatile
        private var INSTANCE: StationRepository? = null

        fun getInstance(
            stationDao: StationDao
        ): StationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StationRepository(stationDao)
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}