package dev.shuanghua.ui.screen.city

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


internal const val provinceNameArg = "provinceName"

fun NavController.openCityList(provinceName: String) {
	this.navigate("city_route/$provinceName")
}

fun NavGraphBuilder.cityScreen(
	onBackClick: () -> Unit,
	openFavoriteScreen: () -> Unit,
) {
	composable(
		route = "city_route/{$provinceNameArg}",
		arguments = listOf( // 传递参数
			navArgument(provinceNameArg) { type = NavType.StringType }
		)
	) {
		CitiesScreen(
			navigateToFavoriteScreen = openFavoriteScreen,
			onBackClick = onBackClick
		)
	}
}