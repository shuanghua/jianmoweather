package dev.shuanghua.weather.data.android.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.network.ParamsSerializationDataSource
import dev.shuanghua.weather.data.android.network.ParamsDataSource

@Module
@InstallIn(SingletonComponent::class)
interface ParamsModule {
    @Binds
    fun ParamsSerializationDataSource.binds(): ParamsDataSource
}