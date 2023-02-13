package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
	private val dataStore: AppDataStoreDataSource,
) {
	fun getTheme(): Flow<ThemeConfig> = dataStore.theme
	suspend fun setTheme(themeConfig: ThemeConfig) = dataStore.setThemeMode(themeConfig)
}