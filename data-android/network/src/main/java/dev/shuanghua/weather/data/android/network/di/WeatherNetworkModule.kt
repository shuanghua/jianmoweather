package dev.shuanghua.weather.data.android.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.RetrofitNetworkDataSource

@Module
@InstallIn(SingletonComponent::class)
interface WeatherNetworkModule {

    @Binds
    fun RetrofitNetworkDataSource.binds(): NetworkDataSource
}