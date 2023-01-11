package dev.shuanghua.weather.data.android.datastore.location


import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.datastore.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineLocationDataSource @Inject constructor(
    private val dataStore: DataStore<Location>,
) {

    val lastLocationData: Flow<OfflineLocation> =
        dataStore.data.map { locationDataStore ->
            OfflineLocation(
                cityName = locationDataStore.cityName,
                latitude = locationDataStore.lat,
                longitude = locationDataStore.lon,
                district = locationDataStore.district,
                address = locationDataStore.address,
            )
        }

    suspend fun setOfflineLocation(offlineLocation: OfflineLocation) {
        dataStore.updateData { location ->
            location.toBuilder()
                .setCityName(offlineLocation.cityName)
                .setLat(offlineLocation.latitude)
                .setLon(offlineLocation.longitude)
                .setDistrict(offlineLocation.district)
                .setAddress(offlineLocation.address).build()
        }
    }
}