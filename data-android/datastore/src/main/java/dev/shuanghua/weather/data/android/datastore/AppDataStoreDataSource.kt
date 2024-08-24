package dev.shuanghua.weather.data.android.datastore

import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppDataStoreDataSource {
	fun getLocationFlow(): Flow<Location>
	fun theme(): Flow<ThemeConfig>
	suspend fun setThemeMode(themeConfig: ThemeConfig)
	suspend fun saveLocation(location: Location)
}

class AppDataStoreDataSourceImpl(
	private val dataStore: DataStore<AppPreferences>,
) : AppDataStoreDataSource {
	override fun getLocationFlow(): Flow<Location> = dataStore.data
		.map { it: AppPreferences ->
			Location(
				cityName = it.location.cityName,
				latitude = it.location.lat,
				longitude = it.location.lon,
				district = it.location.district,
				address = it.location.address,
			)
		}

	override fun theme(): Flow<ThemeConfig> = dataStore.data
		.map { appData: AppPreferences ->
			when (appData.theme) {
				0 -> ThemeConfig.FOLLOW_SYSTEM
				1 -> ThemeConfig.LIGHT
				2 -> ThemeConfig.Dark
				else -> ThemeConfig.FOLLOW_SYSTEM
			}
		}

	override suspend fun setThemeMode(themeConfig: ThemeConfig) {
		dataStore.updateData { appData: AppPreferences ->
			appData.toBuilder().setTheme(
				when (themeConfig) {
					ThemeConfig.FOLLOW_SYSTEM -> 0
					ThemeConfig.LIGHT -> 1
					ThemeConfig.Dark -> 2
				}
			).build()
		}
	}

	override suspend fun saveLocation(location: Location) {
		dataStore.updateData { appPreferences: AppPreferences ->
			val locationInfo = appPreferences.location.copy {
				cityName = location.cityName
				lat = location.latitude
				lon = location.longitude
				district = location.district
				address = location.address
			}
			appPreferences.toBuilder().setLocation(locationInfo).build()
		}
	}
}