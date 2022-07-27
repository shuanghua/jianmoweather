package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.dao.DistrictDao
import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.District
import dev.shuanghua.weather.data.db.entity.Station
import dev.shuanghua.weather.data.network.ShenZhenService
import timber.log.Timber
import javax.inject.Inject

class DistrictRepository @Inject constructor(
    private val service: ShenZhenService,
    private val districtDao: DistrictDao,
    private val stationDao: StationDao
) {
    /**
     * 因为站点的数据不经常变动，建议在首次安装APP时调用，同时提供手动刷新操作
     */
    suspend fun updateStationList(param: String) {
        val districts = service.getStationList(param).body()?.data?.list
        if (districts.isNullOrEmpty()) throw Exception("服务器数据为空！")
        val districtList = ArrayList<District>()
        val stationList = ArrayList<Station>()

        districts.forEach { districtData ->
            districtList.add(District(districtData.name))
            districtData.list.forEach {
                stationList.add(
                    Station(
                        districtName = districtData.name,
                        stationId = it.stationId,
                        stationName = it.stationName
                    )
                )
            }
        }
        Timber.d("--->>$districtList")
        Timber.d("--->>$stationList")
        districtDao.insertDistricts(districtList)
        stationDao.insertStations(stationList)
    }
}