package dev.shuanghua.weather.data.repo.province

import dev.shuanghua.weather.shared.Result

import dev.shuanghua.weather.data.db.entity.Province
import kotlinx.coroutines.flow.Flow

class ProvinceRepository(
    private val localDataSource: ProvinceLocalDataSource,
    private val remoteDataSource: ProvinceRemoteDataSource
) {

    suspend fun updateProvince() {
        when (val remoteResult = remoteDataSource.loadProvince()) {
            is Result.Success -> localDataSource.insertProvinces(remoteResult.data)
            is Result.Error -> throw Throwable("省份数据更新出错!")
        }

    }

    fun observerProvinces(): Flow<List<Province>> = localDataSource.observerProvinces()


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

