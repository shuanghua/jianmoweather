package dev.shuanghua.weather.data.android.domain.di

import dev.shuanghua.weather.data.android.domain.GetFavoriteDetailWeatherUseCase
import dev.shuanghua.weather.data.android.domain.ObserverDistrictUseCase
import dev.shuanghua.weather.data.android.domain.ObserverProvinceUseCase
import dev.shuanghua.weather.data.android.domain.SaveStationToFavoriteUseCase
import dev.shuanghua.weather.data.android.domain.UpdateDistrictUseCase
import dev.shuanghua.weather.data.android.domain.UpdateFavoriteCitiesWeatherUseCase
import dev.shuanghua.weather.data.android.domain.UpdateFavoriteStationsWeatherUseCase
import dev.shuanghua.weather.data.android.domain.UpdateProvinceUseCase
import dev.shuanghua.weather.data.android.domain.UpdateLocationCityWeatherUseCase
import dev.shuanghua.weather.data.android.domain.UpdateStationWeatherUseCase
import dev.shuanghua.weather.data.android.repository.di.repositoryModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
	includes(repositoryModule) // 为 domain 模块添加 repository 模块
	singleOf(::UpdateLocationCityWeatherUseCase) // 创建 UpdateWeatherUseCase 实例
	singleOf(::UpdateStationWeatherUseCase)

	singleOf(::UpdateFavoriteStationsWeatherUseCase)
	singleOf(::UpdateFavoriteCitiesWeatherUseCase)
	singleOf(::GetFavoriteDetailWeatherUseCase)
	singleOf(::UpdateFavoriteStationsWeatherUseCase)
	singleOf(::UpdateFavoriteCitiesWeatherUseCase)
	singleOf(::SaveStationToFavoriteUseCase)

	singleOf(::UpdateProvinceUseCase)
	singleOf(::ObserverProvinceUseCase)

	singleOf(::ObserverDistrictUseCase)
	singleOf(::UpdateDistrictUseCase)
}