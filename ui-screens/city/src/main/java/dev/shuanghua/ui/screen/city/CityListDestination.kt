package dev.shuanghua.ui.screen.city

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


internal const val provinceIdArg = "provinceId"
internal const val provinceNameArg = "provinceName"

fun NavController.openCityList(
    provinceId: String,
    provinceName: String
) {
    this.navigate("city_route/$provinceId/$provinceName")
}

fun NavGraphBuilder.cityScreen(
    onBackClick: () -> Unit,
    openFavoriteScreen: () -> Unit,
) {
    composable(
        route = "city_route/{$provinceIdArg}/{$provinceNameArg}",
        arguments = listOf(
            navArgument(provinceIdArg) { type = NavType.StringType },
            navArgument(provinceNameArg) { type = NavType.StringType },
        )
    ) {
        CityListRoute(
            navigateToFavoriteScreen = openFavoriteScreen,
            onBackClick = onBackClick
        )
    }
}