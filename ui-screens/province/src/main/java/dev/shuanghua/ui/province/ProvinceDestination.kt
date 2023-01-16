package dev.shuanghua.ui.province

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
        ProvinceScreen(
            onBackClick = onBackClick,
            navigateToCityScreen = openCityScreen,
        )
    }
}