package com.moshuanghua.jianmoweather.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jianmoweather.data.db.dao.CityDao
import jianmoweather.data.db.dao.ParamsDao
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.repo.city.CityRemoteDataSource
import jianmoweather.data.repo.city.CityRepository
import jianmoweather.data.repo.favorite.FavoriteLocalDataSource
import jianmoweather.data.repo.favorite.FavoriteRepository
import jianmoweather.data.repo.location.LocationDataSource
import jianmoweather.data.repo.location.LocationRepository
import jianmoweather.data.repo.location.ParamsRepository
import jianmoweather.data.repo.province.ProvinceLocalDataSource
import jianmoweather.data.repo.province.ProvinceRemoteDataSource
import jianmoweather.data.repo.province.ProvinceRepository
import jianmoweather.data.repo.station.StationRepository
import jianmoweather.data.repo.weather.WeatherRemoteDataSource
import jianmoweather.data.repo.weather.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	// -----------------------------------Repository ↙------------------------------------------
	@Provides
	fun provideParamsRepository(
		cityRemoteDataSource: CityRemoteDataSource,
		paramsDao: ParamsDao
	) = ParamsRepository.getInstance(
		paramsDao,
		cityRemoteDataSource
	)

	@Provides
	fun provideLocationRepository(
		locationDataSource: LocationDataSource
	) = LocationRepository.getInstance(locationDataSource)


	@Provides
	fun provideWeatherRepository(
		weatherDao: WeatherDao,
		weathRemoteDataSource: WeatherRemoteDataSource,
	) = WeatherRepository.getInstance(
		weatherDao,
		weathRemoteDataSource
	)


	@Provides
	fun provideProvinceRepository(
		cityDao: CityDao,
		provinceRemoteDataSource: ProvinceRemoteDataSource
	): ProvinceRepository {
		return ProvinceRepository(
			ProvinceLocalDataSource(cityDao),
			provinceRemoteDataSource
		)
	}

	@Singleton
	@Provides
	fun provideCityRepository(cityRemoteDataSource: CityRemoteDataSource): CityRepository {
		return CityRepository.getInstance(cityRemoteDataSource)
	}


	@Singleton
	@Provides
	fun provideFavoriteRepository(cityDao: CityDao): FavoriteRepository {
		return FavoriteRepository(FavoriteLocalDataSource.getInstance(cityDao))
	}

	@Provides
	fun provideStationRepository(service: ShenZhenService): StationRepository {
		return StationRepository.getInstance(service)
	}
}