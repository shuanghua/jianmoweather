package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.Province
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinces: List<Province>)

    @Query("SELECT * FROM province")
    fun observerProvinces(): Flow<List<Province>>
}