package dev.shuanghua.weather.data.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.datastore.location.LocationSerializer
import dev.shuanghua.weather.data.android.datastore.location.DataStoreLocationDataSource
import dev.shuanghua.weather.data.android.datastore.settings.SettingsDataSource
import dev.shuanghua.weather.data.android.datastore.settings.SettingsSerializer
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
    ): DataStore<Settings> = DataStoreFactory.create(
        serializer = settingsSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.dataStoreFile("settings.pb")
    }


    @Provides
    @Singleton
    fun providesLocationDataStore(
        @ApplicationContext context: Context, locationSerializer: LocationSerializer,
    ): DataStore<Location> = DataStoreFactory.create(
        serializer = locationSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.dataStoreFile("location.pb")
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        settings: DataStore<Settings>,
    ) = SettingsDataSource(settings)

    @Provides
    @Singleton
    fun provideOfflineLocationDataSource(
        offlineLocation: DataStore<Location>,
    ) = DataStoreLocationDataSource(
        dataStore = offlineLocation
    )
}