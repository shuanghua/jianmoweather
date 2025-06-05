package dev.shuanghua.ui.screen.city

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data class CityRoute(val provinceName: String)

fun NavController.openCityList(provinceName: String) {
//	navigate("city_route/$provinceName")
	navigate(CityRoute(provinceName))
}

fun NavGraphBuilder.cityScreen(
	onBackClick: () -> Unit,
	openFavoriteScreen: () -> Unit,
) {
	composable<CityRoute> {
//		val provinceName = entry.arguments?.getString("provinceName").orEmpty()  // 在 Ui 中使用
//		private val provinceName: String = checkNotNull(savedStateHandle["provinceName"]) // 在 ViewModel 中使用

		CitiesScreen(
			navigateToFavoriteScreen = openFavoriteScreen,
			onBackClick = onBackClick
		)
	}
}