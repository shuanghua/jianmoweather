package dev.shuanghua.weather.data.db.dao

import androidx.room.*
import dev.shuanghua.weather.data.db.pojo.LastRequestParams
import dev.shuanghua.weather.data.network.MainWeatherParam
import dev.shuanghua.weather.data.network.OuterParam

@Dao
abstract class ParamsDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertLocationCityWeatherRequestParam(
        outerParam: OuterParam,
        innerParam: MainWeatherParam
    )

    @Transaction
    @Query("SELECT * FROM OuterParam")
    abstract suspend fun _getOuterParam(): OuterParam

    @Transaction
    @Query("SELECT * FROM MainWeatherParam")
    abstract suspend fun _getMainWeatherParam(): MainWeatherParam

    suspend fun getLastLocationCityWeatherRequestParam(): LastRequestParams {
        val outerParam = _getOuterParam()
        val innerParam = _getMainWeatherParam()
        return LastRequestParams(lastOuterParam = outerParam, lastMainWeatherParam = innerParam)
    }
}