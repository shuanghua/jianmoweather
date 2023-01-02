package dev.shuanghua.weather.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.repo.OfflineWeatherRepository
import dev.shuanghua.weather.data.repo.RetrofitWeatherRepository
import dev.shuanghua.weather.data.repo.SZNetworkDataSource
import dev.shuanghua.weather.data.repo.WeatherRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindSZNetworkDataSource(
        retrofitWeatherRepository: RetrofitWeatherRepository,
    ): SZNetworkDataSource

    @Binds
    fun bindWeatherRepository(
        offlineWeatherRepository: OfflineWeatherRepository,
    ): WeatherRepository
}