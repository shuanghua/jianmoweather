package dev.shuanghua.ui.screen.favorite

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val favoritesNavigation = "favorites_navigation"
const val favoritesRoute = "favorites_route"

fun NavGraphBuilder.favoriteScreen(
    openProvinceScreen: () -> Unit,
    openFavoriteWeatherScreen: (String, String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    // 别的页面打开自己
    navigation(// 有 navigation时，以 destination 为目标
        route = favoritesNavigation,
        startDestination = favoritesRoute // 要显示的页面，和 composable.route对应
    ) {
        composable(route = favoritesRoute) {
            FavoritesRoute(
                openProvinceScreen = openProvinceScreen,
                openFavoriteWeatherScreen = openFavoriteWeatherScreen
            )
        }

        nestedGraphs()
    }
}