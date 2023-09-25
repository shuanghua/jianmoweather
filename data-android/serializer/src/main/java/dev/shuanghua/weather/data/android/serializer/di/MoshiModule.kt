package dev.shuanghua.weather.data.android.serializer.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.serializer.model.AppDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {
    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()


    @Singleton
    @Provides
    fun providesMoshiMapAdapter(
        moshi: Moshi
    ): JsonAdapter<Map<String, Any>> = moshi.adapter(
        Types.newParameterizedType(
            Map::class.java, String::class.java, Any::class.java
        )
    )

    @Singleton
    @Provides
    fun providesAppSettingsAdapter(
        moshi: Moshi
    ): JsonAdapter<AppDataStore> =
        moshi.adapter(AppDataStore::class.java)
}