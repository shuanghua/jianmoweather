package dev.shuanghua.weather.data.android.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.network.SzwNetworkDataSource
import dev.shuanghua.weather.data.android.network.SzwNetworkDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun SzwNetworkDataSourceImpl.binds(): SzwNetworkDataSource
}