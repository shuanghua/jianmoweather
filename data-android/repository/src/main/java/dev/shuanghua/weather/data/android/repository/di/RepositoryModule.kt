package dev.shuanghua.weather.data.android.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.database.dao.ProvinceDao
import dev.shuanghua.weather.data.android.database.dao.StationDao
import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.ProvinceRepository
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideParamsRepository() = ParamsRepository()


    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherDao: WeatherDao,
        networkDataSource: NetworkDataSource,
        dispatchers: AppCoroutineDispatchers
    ) = WeatherRepository(
        weatherDao,
        networkDataSource,
        dispatchers
    )


    @Singleton
    @Provides
    fun provideProvinceRepository(
        provinceDao: ProvinceDao,
        networkDataSource: NetworkDataSource

    ) = ProvinceRepository(
        provinceDao,
        networkDataSource
    )


    @Singleton
    @Provides
    fun provideStationRepository(
        stationDao: StationDao,
    ) = StationRepository.getInstance(stationDao)
}