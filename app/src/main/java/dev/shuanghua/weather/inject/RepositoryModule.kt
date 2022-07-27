package dev.shuanghua.weather.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.db.dao.*
import dev.shuanghua.weather.data.network.ShenZhenService
import dev.shuanghua.weather.data.repo.*
import dev.shuanghua.weather.data.repo.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // -----------------------------------Repository ↙------------------------------------------
    @Provides
    fun provideParamsRepository(
        paramsDao: ParamsDao
    ) = ParamsRepository.getInstance(paramsDao)

    @Provides
    fun provideLocationRepository(
        locationDataSource: LocationDataSource
    ) = LocationRepository.getInstance(locationDataSource)

    @Provides
    fun provideWeatherRepository(
        weatherDao: WeatherDao,
        stationDao: StationDao,
        service: ShenZhenService,
    ) = WeatherRepository.getInstance(
        weatherDao,
        stationDao,
        service
    )

    @Provides
    fun provideProvinceRepository(
        provinceDao: ProvinceDao,
        service: ShenZhenService
    ) = ProvinceRepository(
        provinceDao,
        service
    )

    @Singleton
    @Provides
    fun provideCityRepository(
        service: ShenZhenService
    ) = CityRepository.getInstance(service)

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        service: ShenZhenService,
        favoriteDao: FavoriteDao
    ) = FavoriteRepository.getInstance(
        service,
        favoriteDao
    )

    @Provides
    fun provideDistrictRepository(
        service: ShenZhenService,
        districtDao: DistrictDao,
        stationDao: StationDao
    ) = DistrictRepository(
        service,
        districtDao,
        stationDao
    )

    @Provides
    fun provideStationRepository(
        stationDao: StationDao
    ) = StationRepository.getInstance(stationDao)

}