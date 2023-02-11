package dev.shuanghua.ui.screen.province

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavController.openProvinceList() {
    this.navigate("province_route")
}

fun NavGraphBuilder.provinceScreen(
    onBackClick: () -> Unit,
    openCityScreen: (String, String) -> Unit,
) {
    composable(route = "province_route") {
        ProvinceListRoute(
            onBackClick = onBackClick,
            openCityListScreen = openCityScreen,
        )
    }
}