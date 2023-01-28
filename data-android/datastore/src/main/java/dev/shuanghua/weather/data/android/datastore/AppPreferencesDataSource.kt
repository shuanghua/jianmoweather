package dev.shuanghua.weather.data.android.datastore

import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.model.Location
import dev.shuanghua.weather.data.android.model.ThemeConfig
import dev.shuanghua.weather.data.android.serializer.model.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<AppDataStore>,
) {

    val getLocationFlow: Flow<Location> = dataStore.data
        .map {
            Location(
                cityName = it.cityName,
                latitude = it.cityName,
                longitude = it.cityName,
                district = it.cityName,
                address = it.cityName,
            )
        }

    val theme: Flow<ThemeConfig> = dataStore.data
        .map { appData: AppDataStore ->
            when (appData.theme) {
                0 -> ThemeConfig.FOLLOW_SYSTEM
                1 -> ThemeConfig.LIGHT
                2 -> ThemeConfig.Dark
                else -> ThemeConfig.FOLLOW_SYSTEM
            }
        }

    suspend fun setThemeMode(themeConfig: ThemeConfig) {
        dataStore.updateData { appData: AppDataStore ->
            appData.copy(
                theme = when (themeConfig) {
                    ThemeConfig.FOLLOW_SYSTEM -> 0
                    ThemeConfig.LIGHT -> 1
                    ThemeConfig.Dark -> 2
                }
            )
        }
    }

    suspend fun saveLocation(location: Location) {
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