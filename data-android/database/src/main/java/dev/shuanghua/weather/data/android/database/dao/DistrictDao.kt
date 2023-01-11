package dev.shuanghua.weather.data.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shuanghua.weather.data.android.database.entity.DistrictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDistricts(districts: List<DistrictEntity>)

    @Query("SELECT * FROM district")
    fun observerDistricts(): Flow<List<DistrictEntity>>
}