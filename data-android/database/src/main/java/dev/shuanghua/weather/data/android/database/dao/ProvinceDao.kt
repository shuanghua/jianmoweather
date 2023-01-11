package dev.shuanghua.weather.data.android.database.dao

import androidx.room.*
import dev.shuanghua.weather.data.android.database.entity.ProvinceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinces: List<ProvinceEntity>)

    @Query("SELECT * FROM province")
    fun observerProvinces(): Flow<List<ProvinceEntity>>
}