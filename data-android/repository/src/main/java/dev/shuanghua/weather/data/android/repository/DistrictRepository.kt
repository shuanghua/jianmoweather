package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.database.dao.DistrictDao
import dev.shuanghua.weather.data.android.database.dao.StationDao
import dev.shuanghua.weather.data.android.database.entity.DistrictEntity
import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.database.entity.asExternalModel
import dev.shuanghua.weather.data.android.model.District
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.network.SzwNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DistrictRepository @Inject constructor(
	private val network: SzwNetworkDataSource,
	private val districtDao: DistrictDao,
	private val stationDao: StationDao
) {
	/**
	 * 因为站点的数据不经常变动，建议在首次安装APP时调用，同时提供手动刷新操作
	 */
	suspend fun updateStationList(param: DistrictParams) {
		val districts = network.getDistrictWithStationList(param) ?: return
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

	fun observerDistricts(): Flow<List<District>> =
		districtDao.observerDistricts().map { districtList ->
			districtList.map(DistrictEntity::asExternalModel)
		}
}