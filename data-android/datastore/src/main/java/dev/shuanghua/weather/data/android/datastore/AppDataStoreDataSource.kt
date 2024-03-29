package dev.shuanghua.weather.data.android.datastore

import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.datastore.model.DataStoreModel
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppDataStoreDataSource{
	fun getLocationFlow(): Flow<Location>
	fun theme(): Flow<ThemeConfig>
	suspend fun setThemeMode(themeConfig: ThemeConfig)
	suspend fun saveLocation(location: Location)
}

class AppDataStoreDataSourceImpl(
	private val dataStore: DataStore<DataStoreModel>,
) :AppDataStoreDataSource{
	override fun getLocationFlow(): Flow<Location> = dataStore.data
		.map {
			Location(
				cityName = it.cityName,
				latitude = it.cityName,
				longitude = it.cityName,
				district = it.cityName,
				address = it.cityName,
			)
		}

	override fun theme(): Flow<ThemeConfig> = dataStore.data
		.map { appData: DataStoreModel ->
			when (appData.theme) {
				0 -> ThemeConfig.FOLLOW_SYSTEM
				1 -> ThemeConfig.LIGHT
				2 -> ThemeConfig.Dark
				else -> ThemeConfig.FOLLOW_SYSTEM
			}
		}

	override suspend fun setThemeMode(themeConfig: ThemeConfig) {
		dataStore.updateData { appData: DataStoreModel ->
			appData.copy(
				theme = when (themeConfig) {
					ThemeConfig.FOLLOW_SYSTEM -> 0
					ThemeConfig.LIGHT -> 1
					ThemeConfig.Dark -> 2
				}
			)
		}
	}

	override suspend fun saveLocation(location: Location) {
		dataStore.updateData {
			it.copy(
				cityName = location.cityName,
				latitude = location.latitude,
				longitude = location.longitude,
				district = location.district,
				address = location.address
			)
		}
	}
}