package dev.shuanghua.weather.data.android.database.dao

import androidx.room.*
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityList(cities: List<CityEntity>)

    @Transaction
    @Query("SELECT * FROM city WHERE provinceName =:provinceName ")
    fun observerCityList(provinceName: String): Flow<List<CityEntity>>
}