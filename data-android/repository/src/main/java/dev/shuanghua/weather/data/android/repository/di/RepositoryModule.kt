package dev.shuanghua.weather.data.android.repository.di

import dev.shuanghua.weather.data.android.database.di.dataBaseModule
import dev.shuanghua.weather.data.android.datastore.di.dataStoreModule
import dev.shuanghua.weather.data.android.location.di.locationModule
import dev.shuanghua.weather.data.android.network.di.networkModule
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.FavoritesRepositoryImpl
import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.LocationRepositoryImpl
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.data.android.repository.ProvinceCityRepositoryImpl
import dev.shuanghua.weather.data.android.repository.SettingsRepository
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.data.android.repository.StationRepositoryImpl
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepositoryImpl
import org.koin.dsl.module


val repositoryModule = module {
	includes(networkModule, dataBaseModule, dataStoreModule, locationModule)
	single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
	single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }
	single<ParamsRepository> { ParamsRepository() }
	single<ProvinceCityRepository> { ProvinceCityRepositoryImpl(get(), get(), get(), get(), get()) }
	single<StationRepository> { StationRepositoryImpl(get(), get(), get()) }
	single<LocationRepository> { LocationRepositoryImpl(get(), get(), get()) }
	single<SettingsRepository> { SettingsRepository(get()) }
}
