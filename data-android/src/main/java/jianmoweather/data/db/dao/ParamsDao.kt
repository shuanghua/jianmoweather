package jianmoweather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import jianmoweather.data.db.entity.Params
import kotlinx.coroutines.flow.Flow

@Dao
interface ParamsDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)//冲突时替换，不冲突往下插入
    suspend fun insertWeatherParam(param: Params)

    @Transaction
    @Query("SELECT * FROM Params WHERE isLocation = :isLocation")
    suspend fun findWeatherRequestParam(isLocation: Int): Params?

    @Transaction
    @Query("SELECT * FROM Params WHERE isLocation = :isLocation")
    fun observerRequestParam(isLocation: Int): Flow<Params?>
}