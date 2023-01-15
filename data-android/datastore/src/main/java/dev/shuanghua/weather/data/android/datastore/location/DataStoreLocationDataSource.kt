package dev.shuanghua.weather.data.android.datastore.location


import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.datastore.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreLocationDataSource @Inject constructor(
    private val dataStore: DataStore<Location>,
) {

    val dataStoreLocation: Flow<DataStoreLocation> =
        dataStore.data.map { location ->
            DataStoreLocation(
                cityName = location.cityName,
                latitude = location.lat,
                longitude = location.lon,
                district = location.district,
                address = location.address,
            )
        }

    suspend fun setDataStoreLocation(dataStoreLocation: DataStoreLocation) {
        dataStore.updateData { location ->
            location.toBuilder()
                .setCityName(dataStoreLocation.cityName)
                .setLat(dataStoreLocation.latitude)
                .setLon(dataStoreLocation.longitude)
                .setDistrict(dataStoreLocation.district)
                .setAddress(dataStoreLocation.address).build()
        }
    }
}