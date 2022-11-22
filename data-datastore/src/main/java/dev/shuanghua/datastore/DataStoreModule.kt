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
import dev.shuanghua.datastore.SettingsDataStore
import dev.shuanghua.datastore.SettingsSerializer
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
        @ApplicationContext context: Context,
//        @Dispatcher(JianMoDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        settingsSerializer: SettingsSerializer
    ): DataStore<SettingsDataStore> = DataStoreFactory.create(
        serializer = settingsSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.dataStoreFile("settings.pb")
    }
}