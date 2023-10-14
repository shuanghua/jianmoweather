package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import dev.shuanghua.weather.data.android.location.Result
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class LocationRepository(
	private val networkDataSource: NetworkLocationDataSource,
	private val dataStore: AppDataStoreDataSource,
	private val dispatchers: AppDispatchers,
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
	suspend fun getLocalLocation(): Location {
		return dataStore.getLocationFlow.first()
	}

	suspend fun saveLocationToDataStore(location: Location) {
		dataStore.saveLocation(location)
	}
}