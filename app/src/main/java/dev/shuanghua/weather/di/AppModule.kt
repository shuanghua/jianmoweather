package dev.shuanghua.weather.di

import dev.shuanghua.ui.screen.city.CitiesViewModel
import dev.shuanghua.ui.screen.district.DistrictListViewModel
import dev.shuanghua.ui.screen.favorite.FavoriteViewModel
import dev.shuanghua.ui.screen.favorites.detail.FavoritesDetailViewModel
import dev.shuanghua.ui.screen.province.ProvincesViewModel
import dev.shuanghua.ui.screen.setting.SettingsViewModel
import dev.shuanghua.ui.screen.station.StationViewModel
import dev.shuanghua.ui.screen.weather.WeatherViewModel
import dev.shuanghua.weather.data.android.domain.di.useCaseModule
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
	
	factory {
		AppDispatchers(
			io = Dispatchers.IO,
			cpu = Dispatchers.Default,
			main = Dispatchers.Main
		)
	}

	includes(useCaseModule) // 使用 domain module 中的 主题相关的 UseCase

	viewModelOf(::MainViewModel)

	viewModelOf(::WeatherViewModel)

	viewModelOf(::FavoriteViewModel)
	viewModelOf(::FavoritesDetailViewModel)

	viewModelOf(::SettingsViewModel)

	viewModelOf(::ProvincesViewModel)
	viewModelOf(::CitiesViewModel)

	viewModelOf(::DistrictListViewModel)
	viewModelOf(::StationViewModel)
}