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

    //如果设置中有自动刷新开关的情况
//    val autoRefresh: Flow<Int> = settingsDataStore.data
//        .map { settings ->
//            settings.autoRefresh
//        }

    suspend fun setThemeMode(tm: Int) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setThemeMode(tm)
                .build()
        }
    }
}