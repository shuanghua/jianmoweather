package dev.shuanghua.ui.screen.favorites.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data class FavoriteDetailRoute(val cityId: String, val stationName: String)

/**
 * 打开
 */
fun NavController.openFavoriteWeather(
	cityId: String,
	stationName: String,
) {
	navigate(FavoriteDetailRoute(cityId, stationName))
}

/**
 * 页面地址 和 页面事件返回
 */
fun NavGraphBuilder.favoriteWeatherScreen(
	onBackClick: () -> Unit,
	openAirDetailsWebScreen: (String) -> Unit,
) {
	composable<FavoriteDetailRoute>{
		FavoritesDetailScreen(
			onBackClick = onBackClick,
			openAirDetailsWebScreen = openAirDetailsWebScreen
		)
	}
}