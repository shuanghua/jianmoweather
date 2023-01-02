package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(cities: List<CityEntity>)

    @Query("SELECT * FROM city")
    fun observerCities(): Flow<List<CityEntity>>
}