package dev.shuanghua.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.shuanghua.ui.screen.city.cityScreen
import dev.shuanghua.ui.screen.city.openCityList
import dev.shuanghua.ui.screen.district.districtScreen
import dev.shuanghua.ui.screen.district.openDistrictList
import dev.shuanghua.ui.screen.favorite.favoriteScreen
import dev.shuanghua.ui.screen.favorite.favoritesRoute
import dev.shuanghua.ui.screen.more.moreScreen
import dev.shuanghua.ui.screen.province.openProvinceList
import dev.shuanghua.ui.screen.province.provinceScreen
import dev.shuanghua.ui.screen.favorites.weather.favoriteWeatherScreen
import dev.shuanghua.ui.screen.favorites.weather.openFavoriteWeather
import dev.shuanghua.ui.screen.setting.openSettings
import dev.shuanghua.ui.screen.setting.settingsScreen
import dev.shuanghua.ui.screen.station.openStationList
import dev.shuanghua.ui.screen.station.stationScreen
import dev.shuanghua.ui.screen.weather.weatherNavigation
import dev.shuanghua.ui.screen.weather.weatherScreen
import dev.shuanghua.ui.screen.web.openWeb
import dev.shuanghua.ui.screen.web.webScreen
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi

/**
 * 传值和导航都在此处处理
 */
@Composable
fun AppNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {

	NavHost(
		navController = navController,
		startDestination = weatherNavigation, // 告诉 NavBottomBar 进入应用后要打开的哪个item navigation route ,
		modifier = modifier
	) {
		favoriteScreen(
			openProvinceScreen = { navController.openProvinceList() },
			openFavoriteWeatherScreen = { cityId, stationName ->
				navController.openFavoriteWeather(
					cityId,
					stationName
				)
			},
			nestedGraphs = {
				provinceScreen(
					onBackClick = { navController.popBackStack() },
					openCityScreen = { provinceName ->
						navController.openCityList(provinceName)
					}
				)

				cityScreen(
					onBackClick = { navController.popBackStack() },
					openFavoriteScreen = {
						// 如果 inclusive 为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
						navController.popBackStack(route = favoritesRoute, inclusive = false)
					}
				)

				favoriteWeatherScreen(
					onBackClick = { navController.popBackStack() },
					openAirDetailsWebScreen = { cityId ->
						navController.openWeb(Api2.getAqiWebUrl(cityId))
					}
				)
			}
		)

		weatherScreen(
			openAirDetails = { cityId ->
				navController.openWeb(Api2.getAqiWebUrl(cityId))
			},
			openDistrictList = { cityId, stationName ->
				navController.openDistrictList(
					cityId,
					stationName
				)
			},
			nestedGraphs = {
				districtScreen(
					onBackClick = { navController.popBackStack() },
					openStationList = { districtName ->
						navController.openStationList(districtName)
					}
				)
				stationScreen(
					onBackClick = { navController.popBackStack() },
					openWeatherScreen = {
						navController.popBackStack(route = "weather_route", inclusive = false)
					}
				)
			}
		)

		moreScreen(
			openWebLink = { url -> navController.openWeb(url) },
			openSettings = { navController.openSettings() },
			nestedGraphs = {
				webScreen(onBackClick = { navController.popBackStack() })
				settingsScreen(onBackClick = { navController.popBackStack() })
			}
		)
	}
}
