package dev.shuanghua.weather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import dev.shuanghua.ui.screen.city.cityScreen
import dev.shuanghua.ui.screen.city.openCityList
import dev.shuanghua.ui.screen.district.districtScreen
import dev.shuanghua.ui.screen.district.openDistrictList
import dev.shuanghua.ui.screen.favorite.FavoriteRoute
import dev.shuanghua.ui.screen.favorite.favoriteScreen
import dev.shuanghua.ui.screen.favorites.detail.favoriteWeatherScreen
import dev.shuanghua.ui.screen.favorites.detail.openFavoriteWeather
import dev.shuanghua.ui.screen.more.MoreRoute
import dev.shuanghua.ui.screen.more.moreScreen
import dev.shuanghua.ui.screen.province.openProvinceList
import dev.shuanghua.ui.screen.province.provinceScreen
import dev.shuanghua.ui.screen.setting.openSettings
import dev.shuanghua.ui.screen.setting.settingsScreen
import dev.shuanghua.ui.screen.station.openStationList
import dev.shuanghua.ui.screen.station.stationScreen
import dev.shuanghua.ui.screen.weather.WeatherRoute
import dev.shuanghua.ui.screen.weather.weatherScreen
import dev.shuanghua.ui.screen.web.openWeb
import dev.shuanghua.ui.screen.web.webScreen
import dev.shuanghua.weather.data.android.network.api.Api2
import kotlinx.serialization.Serializable

/**
 * 用于底部导航的 Route
 */
@Serializable
data object WeatherNavigation

/**
 * 用于底部导航的 Route
 */
@Serializable
data object FavoriteNavigation

/**
 * 用于底部导航的 Route
 */
@Serializable
data object MoreNavigation


/**
 * 传值和导航都在此处处理
 */
@Composable
fun AppNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
) {

	NavHost(
		navController = navController,
		startDestination = WeatherNavigation, // 告诉 NavBottomBar 进入应用后要打开的哪个item navigation route ,
		modifier = modifier
	) {

		navigation<FavoriteNavigation>(startDestination = FavoriteRoute) {
			favoriteScreen(
				openProvinceScreen = { navController.openProvinceList() },
				openFavoriteWeatherScreen = { cityId, stationName ->
					navController.openFavoriteWeather(cityId, stationName)
				},
			)
			provinceScreen(
				onBackClick = { navController.popBackStack() },
				openCityScreen = { provinceName ->
					navController.openCityList(provinceName)
				}
			)

			cityScreen(
				onBackClick = { navController.popBackStack() },
				openFavoriteScreen = {
					// 回退到 FavoriteRoute, 如果 inclusive 为 true, 则目标页面 FavoriteRoute 也会被清除出栈
					navController.popBackStack(route = FavoriteRoute, inclusive = false)
				}
			)

			favoriteWeatherScreen(
				onBackClick = { navController.popBackStack() },
				openAirDetailsWebScreen = { cityId ->
					navController.openWeb(Api2.getAqiWebUrl(cityId))
				}
			)
		}


		navigation<WeatherNavigation>(startDestination = WeatherRoute) {
			weatherScreen(
				openAirDetails = { cityId ->
					navController.openWeb(Api2.getAqiWebUrl(cityId))
				},
				openDistrictList = { cityId, stationName ->
					navController.openDistrictList(cityId, stationName)
				},
			)
			districtScreen(
				onBackClick = { navController.popBackStack() },
				openStationList = { districtName ->
					navController.openStationList(districtName)
				}
			)
			stationScreen(
				onBackClick = { navController.popBackStack() },
				openWeatherScreen = {
					navController.popBackStack(route = WeatherRoute, inclusive = false)
				}
			)
		}


		navigation<MoreNavigation>(startDestination = MoreRoute) {
			moreScreen(
				openWebLink = { url -> navController.openWeb(url) },
				openSettings = { navController.openSettings() },
			)
			webScreen(onBackClick = { navController.popBackStack() })
			settingsScreen(onBackClick = { navController.popBackStack() })
		}
	}
}
