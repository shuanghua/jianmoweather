package dev.shuanghua.weather.data.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shuanghua.weather.data.android.database.entity.SelectedStationEntity
import dev.shuanghua.weather.data.android.database.entity.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Transaction
    @Query("SELECT * FROM station WHERE districtName = :districtName")
    fun observerStations(districtName: String): Flow<List<StationEntity>>

    @Query("SELECT stationId FROM station WHERE stationName = :stationName")
    suspend fun queryStationId(stationName: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedStation(selectedStation: SelectedStationEntity)

    @Query("SELECT * FROM selected_station")
    fun getLastStation(): Flow<SelectedStationEntity?>//判定上次站点是否是自动定位站点
}