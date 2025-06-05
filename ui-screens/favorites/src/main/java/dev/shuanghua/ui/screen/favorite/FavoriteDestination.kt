package dev.shuanghua.ui.screen.favorite

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object FavoriteRoute

fun NavGraphBuilder.favoriteScreen(
	openProvinceScreen: () -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit,
) {
	composable<FavoriteRoute> {
		FavoritesScreen(
			openProvinceScreen = openProvinceScreen,
			openFavoriteWeatherScreen = openFavoriteWeatherScreen
		)
	}
}
