package dev.shuanghua.ui.city

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.shuanghua.core.navigation.AppNavigationDestination

object CityScreenDestination : AppNavigationDestination {
    override val route = "city_route"
    override val destination = "city_destination"
    const val provinceIdArg = "provinceId"
    const val provinceNameArg = "provinceNameArg"
}

fun NavGraphBuilder.cityGraph(
    onBackClick: () -> Unit,
    navigateToFavoriteScreen: () -> Unit
) {
    composable(
        route = CityScreenDestination.route +
                "/{${CityScreenDestination.provinceIdArg}}" +
                "/{${CityScreenDestination.provinceNameArg}}",
        arguments = listOf(
            navArgument(CityScreenDestination.provinceIdArg) {
                type = NavType.StringType
            },
            navArgument(CityScreenDestination.provinceNameArg) {
                type = NavType.StringType
            },
        )
    ) {
        CityScreen(
            navigateToFavoriteScreen = navigateToFavoriteScreen,
            onBackClick = onBackClick
        )
    }
}