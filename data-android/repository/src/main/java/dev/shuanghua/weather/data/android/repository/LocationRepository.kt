package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.location.OfflineLocationDataSource
import dev.shuanghua.weather.data.android.location.LocationResult
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.repository.convert.asDataStoreLocation
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val networkDataSource: NetworkLocationDataSource,
    private val offlineDataSource: OfflineLocationDataSource,
) {

    suspend fun getNetworkLocation(): Location {
        return when (val result = networkDataSource.getOnceLocationResult()) {
            is LocationResult.Error -> throw Throwable(result.errorMessage)
            is LocationResult.Success -> Location(
                cityName = result.data.cityName,
                latitude = result.data.latitude,
                longitude = result.data.longitude,
                district = result.data.district,
                address = result.data.address
            )
        }
    }

    /**
     * 此方法是同步调用，会阻塞当前线程，在UI线程中调用会导致界面卡顿 或者 ANR
     * 在 IO 线程调用可能出现死锁
     */
    suspend fun getOfflineLocation(): Location =
        offlineDataSource.lastLocationData.first().asExternalModel()


    suspend fun setOfflineLocation(location: Location) =
        offlineDataSource.setOfflineLocation(location.asDataStoreLocation())
}