package dev.shuanghua.weather.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.db.dao.ProvinceDao
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.network.ShenZhenService
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.city.CityRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.data.repo.location.LocationDataSource
import dev.shuanghua.weather.data.repo.location.LocationRepository
import dev.shuanghua.weather.data.repo.province.ProvinceRepository
import dev.shuanghua.weather.data.repo.station.StationRepository
import dev.shuanghua.weather.data.repo.weather.WeatherRepository
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
        service: ShenZhenService,
    ) = WeatherRepository.getInstance(
        weatherDao,
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
    fun provideStationRepository(
        service: ShenZhenService
    ) = StationRepository.getInstance(service)

}