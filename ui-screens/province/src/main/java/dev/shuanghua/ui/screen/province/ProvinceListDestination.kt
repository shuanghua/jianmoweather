package dev.shuanghua.ui.screen.province

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ProvinceRoute

fun NavController.openProvinceList() {
//    this.navigate("province_route")
	navigate(ProvinceRoute)
}

fun NavGraphBuilder.provinceScreen(
	onBackClick: () -> Unit,
	openCityScreen: (String) -> Unit,
) {
	composable<ProvinceRoute> {
		ProvinceListRoute(
			onBackClick = onBackClick,
			openCityListScreen = openCityScreen,
		)
	}
}