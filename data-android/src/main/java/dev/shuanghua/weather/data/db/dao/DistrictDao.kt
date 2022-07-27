package dev.shuanghua.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shuanghua.weather.data.db.entity.District
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDistricts(districts: List<District>)

    @Query("SELECT * FROM district")
    fun observerDistricts(): Flow<List<District>>
}