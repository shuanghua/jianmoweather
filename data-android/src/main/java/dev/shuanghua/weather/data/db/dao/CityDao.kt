package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.entity.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(cities: List<City>)

    @Query("SELECT * FROM city")
    fun observerCities(): Flow<List<City>>
}