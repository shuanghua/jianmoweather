package dev.shuanghua.weather.data.repo.province

import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.data.db.entity.City

class ProvinceRepository(
    private val localDataSource: ProvinceLocalDataSource,
    private val remoteDataSource: ProvinceRemoteDataSource
) {
    suspend fun getProvinceList(): Result<List<City>> {
        val remoteResult = remoteDataSource.loadProvince()
        if (remoteResult is Result.Success) {
            localDataSource.insertProvinces(remoteResult.data)
            return Result.Success(localDataSource.queryProvince())
        }
        if (remoteResult is Result.Error) {
            return Result.Error(remoteResult.exception)
        }
        return Result.Success(localDataSource.queryProvince())
    }

    companion object {
        @Volatile
        private var INSTANCE: ProvinceRepository? = null

        fun getInstance(
            localDataSource: ProvinceLocalDataSource,
            remoteDataSource: ProvinceRemoteDataSource
        ): ProvinceRepository {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: ProvinceRepository(
                            localDataSource,
                            remoteDataSource
                        ).also { INSTANCE = it }
                }
        }
    }
}

