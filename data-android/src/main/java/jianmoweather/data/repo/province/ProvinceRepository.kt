package jianmoweather.data.repo.province

import com.moshuanghua.jianmoweather.shared.Result
import jianmoweather.data.db.entity.City

class ProvinceRepository(
    private val localDataSource: ProvinceLocalDataSource,
    private val remoteDataSource: ProvinceRemoteDataSource
) {
    suspend fun getProvinces(): Result<List<City>> {
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

    suspend fun search(
        keywords: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ): Result<MutableList<City>> {
        return remoteDataSource.loadCityByKeyword(keywords, locationCityName, district, lat, lon)
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

