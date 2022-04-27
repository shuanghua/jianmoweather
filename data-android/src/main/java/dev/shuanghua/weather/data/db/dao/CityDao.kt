package dev.shuanghua.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.db.entity.Province

@Dao
interface CityDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinces: List<Province>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityAndCounty(citys: List<City>)

    @Query("SELECT * FROM province")
    fun findProvinceLiveData(): LiveData<List<City>>

    @Query("SELECT * FROM province")
    suspend fun findProvinces(): List<City>
}