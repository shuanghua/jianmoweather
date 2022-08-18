package dev.shuanghua.ui.city

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.shuanghua.core.navigation.AppNavigationDestination
import dev.shuanghua.ui.city.CityScreenDestination.provinceIdArg
import dev.shuanghua.ui.city.CityScreenDestination.provinceNameArg

object CityScreenDestination : AppNavigationDestination {
    override val route = "city_route"
    override val destination = "city_destination"
    const val provinceIdArg = "provinceId"
    const val provinceNameArg = "provinceName"
}

fun NavGraphBuilder.cityScreenGraph(
    onBackClick: () -> Unit,
    navigateToFavoriteScreen: () -> Unit,
) {
    composable(
        route = CityScreenDestination.route + "/{$provinceIdArg}" + "/{$provinceNameArg}",
        arguments = listOf(
            navArgument(provinceIdArg) { type = NavType.StringType },
            navArgument(provinceNameArg) { type = NavType.StringType },
        )
    ) {
        CityScreen(
            navigateToFavoriteScreen = navigateToFavoriteScreen,
            onBackClick = onBackClick
        )
    }
}