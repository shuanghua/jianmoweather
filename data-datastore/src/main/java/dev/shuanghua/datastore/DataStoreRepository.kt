package dev.shuanghua.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val settingsDataStore: DataStore<SettingsDataStore>
) {
    val themeMode: Flow<Int> = settingsDataStore.data
        .map { settings ->
            settings.themeMode
        }

    suspend fun setThemeMode(tm: Int) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setThemeMode(tm)
                .build()
        }
    }
}