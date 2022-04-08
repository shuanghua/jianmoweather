package jianmoweather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cjianmoweather.data.db.entity.Favorite
import jianmoweather.data.db.entity.City
import jianmoweather.data.db.entity.Province

@Dao
interface CityDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinces: List<Province>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityAndCounty(citys: List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesOne(favorite: Favorite)//一个

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesList(favorites: MutableList<Favorite>)//多个

    @Query("SELECT * FROM favorite")
    fun findFavoriteObserver(): LiveData<MutableList<Favorite>>

    @Query("SELECT * FROM favorite")
    fun findFavoritesLiveData(): LiveData<MutableList<Favorite>>

    @Query("SELECT * FROM favorite")
    suspend fun findFavorites(): MutableList<Favorite>

    @Query("SELECT * FROM province")
    fun findProvinceLiveData(): LiveData<List<City>>

    @Query("SELECT * FROM province")
    suspend fun findProvinces(): List<City>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}