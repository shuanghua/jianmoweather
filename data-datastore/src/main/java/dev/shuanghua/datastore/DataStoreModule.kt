package dev.shuanghua.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesSettingsDataStore(
        @ApplicationContext context: Context, settingsSerializer: SettingsSerializer,
    ): DataStore<SettingsDataStore> = DataStoreFactory.create(
        serializer = settingsSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.dataStoreFile("settings.pb")
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataStore: DataStore<SettingsDataStore>,
    ) = DataStoreRepository(dataStore)
}