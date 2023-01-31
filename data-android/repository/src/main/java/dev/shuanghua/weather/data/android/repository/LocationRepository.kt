package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.AppPreferencesDataSource
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import dev.shuanghua.weather.data.android.location.Result
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val networkDataSource: NetworkLocationDataSource,
    private val dataStore: AppPreferencesDataSource,
    private val dispatchers: AppCoroutineDispatchers,
) {

    suspend fun getNetworkLocation(): Location = withContext(dispatchers.io) {
        when (val result = networkDataSource.getNetworkLocation()) {
            is Result.Success -> result.data.asExternalModel()
            is Result.Error -> throw Throwable(result.errorMessage)
        }
    }

    /**
     * 此方法是同步调用，会阻塞当前线程，在UI线程中调用会导致界面卡顿 或者 ANR
     * 在 IO 线程调用可能出现死锁
     */
    suspend fun getLocalLocation(): Location = dataStore.getLocationFlow.first()


    suspend fun saveLocationToDataStore(location: Location) {
        dataStore.saveLocation(location)
    }

}