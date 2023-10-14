package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
	private val dataStore: AppDataStoreDataSource,
) {
	fun getTheme(): Flow<ThemeConfig> = dataStore.theme
	suspend fun setTheme(themeConfig: ThemeConfig) = dataStore.setThemeMode(themeConfig)
}