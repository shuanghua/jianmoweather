package dev.shuanghua.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shuanghua.weather.data.db.entity.AutoLocationStation
import dev.shuanghua.weather.data.db.entity.Station
import dev.shuanghua.weather.data.db.entity.StationReturn
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(districts: List<Station>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutoStations(districts: AutoLocationStation)

    @Query("SELECT * FROM station WHERE districtName = :districtName")
    fun observerStations(districtName: String): Flow<List<Station>>

    @Query("SELECT stationId FROM station WHERE stationName = :stationName")
    suspend fun queryStationId(stationName: String): String

    @Query("SELECT * FROM auto_station WHERE screen = :screen")
    fun observerAutoStation(screen: String = "StationScreen"): Flow<AutoLocationStation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStationReturn(stationType: StationReturn)

    @Query("SELECT * FROM station_return")
    fun observerStationReturn(): Flow<StationReturn?>//判定上次站点是否是自动定位站点

    @Query("SELECT * FROM station_return WHERE screen = :screen")
    suspend fun queryStationReturn(screen: String = "StationScreen"): StationReturn?//判定上次站点是否是自动定位站点
}