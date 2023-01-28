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
import dev.shuanghua.weather.data.android.serializer.model.AppDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesAppDataStore(
        @ApplicationContext context: Context,
        settingsSerializer: AppDataSerializer,
    ): DataStore<AppDataStore> = DataStoreFactory.create(
        serializer = settingsSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.dataStoreFile("app_data.pb")
    }

}