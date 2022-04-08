package jianmoweather.data.repo.city

import com.amap.api.location.AMapLocation
import jianmoweather.data.db.entity.City
import com.moshuanghua.jianmoweather.shared.Result

class CityRepository(private val remoteDataSource: CityRemoteDataSource) {

    suspend fun getCityByProvinceId(
        provinceId: String,
        locationCityName: String,
        district: String,
        lat: String,
        lon: String
    ): Result<MutableList<City>> {
        return remoteDataSource.loadCityByProvinceId(
            provinceId, locationCityName, district, lat, lon
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: CityRepository? = null

        fun getInstance(remoteDataSource: CityRemoteDataSource): CityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CityRepository(remoteDataSource).also { INSTANCE = it }
            }
        }
    }
}

