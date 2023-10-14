package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.DistrictDao
import dev.shuanghua.weather.data.android.database.dao.StationDao
import dev.shuanghua.weather.data.android.database.entity.DistrictEntity
import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.database.entity.asExternalModel
import dev.shuanghua.weather.data.android.model.District
import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.Station
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.model.DistrictStationModel
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.data.android.repository.converter.asWeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DistrictStationRepository {
	suspend fun updateDistrictStationData(param: DistrictParams)

	fun observerDistrictList(): Flow<List<District>>
	fun observerStationList(districtName: String): Flow<List<Station>>

	suspend fun saveSelectedStation(selectedStation: SelectedStation)
	fun observerSelectedStation(): Flow<SelectedStation?>
}

class DistrictStationRepositoryImpl(
	private val network: NetworkDataSource,
	private val districtDao: DistrictDao,
	private val stationDao: StationDao
) : DistrictStationRepository {
	override suspend fun updateDistrictStationData(param: DistrictParams) {
		val districts: List<DistrictStationModel> = network.getStationList(param) ?: return
		val districtList = ArrayList<DistrictEntity>()
		val stationList = ArrayList<StationEntity>()
		districts.forEach { district ->
			districtList.add(DistrictEntity(district.name))
			district.list.forEach {
				stationList.add(
					StationEntity(
						districtName = district.name,
						stationId = it.stationId,
						stationName = it.stationName
					)
				)
			}
		}
		districtDao.insertDistricts(districtList)
		stationDao.insertStations(stationList)
	}

	// 区县
	override fun observerDistrictList(): Flow<List<District>> {
		return districtDao.observerDistricts().map { districtList ->
			districtList.map(DistrictEntity::asExternalModel)
		}
	}

	override fun observerStationList(districtName: String): Flow<List<Station>> {
		return stationDao.observerStations(districtName).map {
			it.map(StationEntity::asExternalModel)
		}
	}

	// 观测站点
	override suspend fun saveSelectedStation(selectedStation: SelectedStation) {
		stationDao.insertSelectedStation(selectedStation.asWeatherEntity())
	}

	override fun observerSelectedStation(): Flow<SelectedStation?> {
		return stationDao.getLastStation().map { it?.asExternalModel() }
	}
}