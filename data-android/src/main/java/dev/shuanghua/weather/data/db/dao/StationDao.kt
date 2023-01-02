package dev.shuanghua.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shuanghua.weather.data.db.entity.AutoLocationStation
import dev.shuanghua.weather.data.db.entity.StationEntity
import dev.shuanghua.weather.data.db.entity.SelectedStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(districts: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutoStations(districts: AutoLocationStation)

    @Query("SELECT * FROM station WHERE districtName = :districtName")
    fun observerStations(districtName: String): Flow<List<StationEntity>>

    @Query("SELECT stationId FROM station WHERE stationName = :stationName")
    suspend fun queryStationId(stationName: String): String

    @Query("SELECT * FROM auto_station WHERE screen = :screen")
    fun observerAutoStation(screen: String = "StationScreen"): Flow<AutoLocationStation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStationReturn(stationType: SelectedStationEntity)

    @Query("SELECT * FROM selected_station")
    fun observerSelectedStation(): Flow<SelectedStationEntity?>//判定上次站点是否是自动定位站点

    @Query("SELECT * FROM selected_station WHERE screen = :screen")
    suspend fun querySelectedStation(screen: String = "StationScreen"): SelectedStationEntity?//判定上次站点是否是自动定位站点
}