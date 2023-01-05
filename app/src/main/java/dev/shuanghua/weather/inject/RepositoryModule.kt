package dev.shuanghua.weather.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.db.dao.*
import dev.shuanghua.weather.data.network.ShenZhenWeatherApi
import dev.shuanghua.weather.data.repo.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // -----------------------------------Repository ↙------------------------------------------
    @Provides
    fun provideParamsRepository(
        paramsDao: ParamsDao,
    ) = ParamsRepository.getInstance(paramsDao)

    @Provides
    fun provideProvinceRepository(
        provinceDao: ProvinceDao,
        service: ShenZhenWeatherApi,
    ) = ProvinceRepository(
        provinceDao,
        service
    )

    @Singleton
    @Provides
    fun provideCityRepository(
        szDataSource: SZNetworkDataSource,
    ) = CityRepository.getInstance(szDataSource)

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        service: ShenZhenWeatherApi,
        favoriteDao: FavoriteDao,
    ) = FavoriteRepository.getInstance(
        service,
        favoriteDao
    )

    @Provides
    fun provideDistrictRepository(
        service: ShenZhenWeatherApi,
        districtDao: DistrictDao,
        stationDao: StationDao,
    ) = DistrictRepository(
        service,
        districtDao,
        stationDao
    )

    @Provides
    fun provideStationRepository(
        stationDao: StationDao,
    ) = StationRepository.getInstance(stationDao)
}