package dev.shuanghua.weather.data.repo

import com.amap.api.location.AMapLocation
import dev.shuanghua.datastore.location.OfflineLocationDataSource
import dev.shuanghua.datastore.model.OfflineLocation
import dev.shuanghua.weather.data.model.LocationResource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val networkDataSource: NetworkLocationDataSource,
    private val offlineDataSource: OfflineLocationDataSource,
) {

    suspend fun getNetworkLocation(): LocationResource {
        return when (val result = networkDataSource.getOnceLocationResult()) {
            is LocationSuccess -> result.data.toLocationResource()
            is LocationError -> throw result.throwable
        }
    }

    /**
     * 此方法是同步调用，会阻塞当前线程，在UI线程中调用会导致界面卡顿 或者 ANR
     * 在 IO 线程调用可能出现死锁
     */
    suspend fun getOfflineLocation(): LocationResource {
        val offlineLocation = offlineDataSource.lastLocationData.first()
        return LocationResource(
            offlineLocation.cityName,
            offlineLocation.latitude,
            offlineLocation.longitude,
            offlineLocation.district,
            offlineLocation.address
        )
    }

    suspend fun setOfflineLocation(locationResource: LocationResource) {
        val offlineLocation = OfflineLocation(
            locationResource.cityName,
            locationResource.latitude,
            locationResource.longitude,
            locationResource.district,
            locationResource.address
        )
        offlineDataSource.setOfflineLocation(offlineLocation)
    }

    private fun AMapLocation.toLocationResource(): LocationResource = LocationResource(
        cityName = city,
        latitude = latitude.toString(),
        longitude = longitude.toString(),
        district = district,
        address = address,
    )
}