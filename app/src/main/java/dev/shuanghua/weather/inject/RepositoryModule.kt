package dev.shuanghua.weather.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.db.dao.ProvinceDao
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.network.ShenZhenService
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.city.CityRemoteDataSource
import dev.shuanghua.weather.data.repo.city.CityRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteLocalDataSource
import dev.shuanghua.weather.data.repo.favorite.FavoriteRemoteDataSource
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.data.repo.location.LocationDataSource
import dev.shuanghua.weather.data.repo.location.LocationRepository
import dev.shuanghua.weather.data.repo.province.ProvinceLocalDataSource
import dev.shuanghua.weather.data.repo.province.ProvinceRemoteDataSource
import dev.shuanghua.weather.data.repo.province.ProvinceRepository
import dev.shuanghua.weather.data.repo.station.StationRepository
import dev.shuanghua.weather.data.repo.weather.WeatherRemoteDataSource
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
        weatherRemoteDataSource: WeatherRemoteDataSource,
    ) = WeatherRepository.getInstance(
        weatherDao,
        weatherRemoteDataSource
    )

    @Provides
    fun provideProvinceRepository(
        provinceDao: ProvinceDao,
        provinceRemoteDataSource: ProvinceRemoteDataSource
    ) = ProvinceRepository(
        ProvinceLocalDataSource(provinceDao),
        provinceRemoteDataSource
    )

    @Singleton
    @Provides
    fun provideCityRepository(
        cityRemoteDataSource: CityRemoteDataSource
    ) = CityRepository.getInstance(cityRemoteDataSource)

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        favoriteRemoteDataSource: FavoriteRemoteDataSource,
        favoriteLocalDataSource: FavoriteLocalDataSource
    ) = FavoriteRepository.getInstance(
        favoriteRemoteDataSource,
        favoriteLocalDataSource
    )

    @Provides
    fun provideStationRepository(
        service: ShenZhenService
    ) = StationRepository.getInstance(service)

}