package dev.shuanghua.weather.data.android.domain.di

import dev.shuanghua.weather.data.android.domain.GetFavoriteCityWeatherUseCase
import dev.shuanghua.weather.data.android.domain.GetFavoriteDetailWeatherUseCase
import dev.shuanghua.weather.data.android.domain.GetFavoriteStationWeatherUseCase
import dev.shuanghua.weather.data.android.domain.ObserverDistrictUseCase
import dev.shuanghua.weather.data.android.domain.ObserverProvinceUseCase
import dev.shuanghua.weather.data.android.domain.SaveStationToFavorite
import dev.shuanghua.weather.data.android.domain.UpdateDistrictUseCase
import dev.shuanghua.weather.data.android.domain.UpdateProvinceUseCase
import dev.shuanghua.weather.data.android.domain.UpdateWeatherUseCase
import dev.shuanghua.weather.data.android.repository.di.repositoryModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
	includes(repositoryModule)
	singleOf(::UpdateWeatherUseCase)

	singleOf(::GetFavoriteCityWeatherUseCase)
	singleOf(::GetFavoriteDetailWeatherUseCase)
	singleOf(::GetFavoriteStationWeatherUseCase)
	singleOf(::SaveStationToFavorite)

	singleOf(::UpdateProvinceUseCase)
	singleOf(::ObserverProvinceUseCase)

	singleOf(::ObserverDistrictUseCase)
	singleOf(::UpdateDistrictUseCase)
}