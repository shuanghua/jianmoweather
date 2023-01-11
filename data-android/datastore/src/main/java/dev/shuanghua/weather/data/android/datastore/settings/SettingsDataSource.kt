package dev.shuanghua.weather.data.android.datastore.settings

import androidx.datastore.core.DataStore
import dev.shuanghua.weather.data.android.datastore.Settings
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataSource @Inject constructor(
    private val dataStore: DataStore<Settings>,
) {
    val theme: Flow<ThemeConfig> = dataStore.data
        .map { settings: Settings ->
            when (settings.theme) {
                0 -> ThemeConfig.FOLLOW_SYSTEM
                1 -> ThemeConfig.LIGHT
                2 -> ThemeConfig.Dark
                else -> ThemeConfig.FOLLOW_SYSTEM
            }
        }

    suspend fun setThemeMode(themeConfig: ThemeConfig) {
        dataStore.updateData { settings: Settings ->
            settings.toBuilder()
                .setTheme(
                    when (themeConfig) {
                        ThemeConfig.FOLLOW_SYSTEM -> 0
                        ThemeConfig.LIGHT -> 1
                        ThemeConfig.Dark -> 2
                    }
                )
                .build()
        }
    }
}