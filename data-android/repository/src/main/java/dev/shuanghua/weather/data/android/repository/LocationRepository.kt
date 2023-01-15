package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.location.DataStoreLocationDataSource
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import dev.shuanghua.weather.data.android.location.Result
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.repository.convert.asDataStoreModel
import dev.shuanghua.weather.data.android.repository.convert.asExternalModel
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val networkDataSource: NetworkLocationDataSource,
    private val dataStore: DataStoreLocationDataSource,
    private val dispatchers: AppCoroutineDispatchers
) {

    suspend fun getNetworkLocation(): Location =
        withContext(dispatchers.io) {
            when (val result = networkDataSource.getNetworkLocation()) {
                is Result.Success -> result.data.asExternalModel()
                is Result.Error -> throw Throwable(result.errorMessage)  // 等待后续优化
            }
        }

    /**
     * 此方法是同步调用，会阻塞当前线程，在UI线程中调用会导致界面卡顿 或者 ANR
     * 在 IO 线程调用可能出现死锁
     */
    suspend fun getDataStoreLocation(): Location =
        withContext(dispatchers.io) {
            dataStore.dataStoreLocation.first().asExternalModel()// 会一直等待 flow 发送数据过来，然后收集
        }


    suspend fun setDataStoreLocation(location: Location) =
        withContext(dispatchers.io) {
            dataStore.setDataStoreLocation(location.asDataStoreModel())
        }

}