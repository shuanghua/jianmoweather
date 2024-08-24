package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import dev.shuanghua.weather.data.android.location.Result
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.repository.converter.asExternalModel
import dev.shuanghua.weather.shared.AppDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber

interface LocationRepository {
	suspend fun getLocation(): Location
	suspend fun getLocationFromNetwork(): Location
	suspend fun getLocationFromDataStore(): Location
	suspend fun saveLocationToDataStore(location: Location)
}

class LocationRepositoryImpl(
	private val networkDataSource: NetworkLocationDataSource,
	private val dataStore: AppDataStoreDataSource,
	private val dispatchers: AppDispatchers,
) : LocationRepository {
	override suspend fun getLocation(
	): Location = supervisorScope {
		// 要求：网络定位出现错误，不影响其它子协程，所以使用 supervisorScope
		val dataStoreLocationDeferred = async { getLocationFromDataStore() }
		val networkLocationDeferred = async { getLocationFromNetwork() }
		val newData = try {
			networkLocationDeferred.await()
		} catch (e: Exception) { // 网络定位出错，使用本地数据，本地数据也可能为空(这里Location 实例不为空，但是其中的值可能为空)
			Timber.d("定位失败:${e.message}")
			null
		}
		val oldData = dataStoreLocationDeferred.await()
		return@supervisorScope when {
			oldData.latitude.isNotEmpty() -> oldData
			newData != null -> newData.also { saveLocationToDataStore(it) }
			else -> defaultLocation.also { saveLocationToDataStore(it) }
		}
	}

	override suspend fun getLocationFromNetwork(
	): Location = withContext(dispatchers.io) {
		when (val result = networkDataSource.getNetworkLocation()) {
			is Result.Success -> result.data.asExternalModel()
			is Result.Error -> throw Exception(result.errorMessage)
		}
	}

	override suspend fun getLocationFromDataStore(
	): Location = withContext(dispatchers.io) {
		dataStore.getLocationFlow().first()
	}

	override suspend fun saveLocationToDataStore(
		location: Location,
	) = withContext(dispatchers.io) {
		dataStore.saveLocation(location)
	}

	companion object {
		private val defaultLocation = Location(
			latitude = "22.538854",
			longitude = "114.010341",
			cityName = "深圳市",
			district = "福田区",
		)
	}
}