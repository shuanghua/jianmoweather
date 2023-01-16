package dev.shuanghua.ui.favorite

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.ui.core.navigation.AppNavigationDestination

object FavoriteDestination : AppNavigationDestination {
    override val route = "favorite_route"
    override val destination = "favorite_destination"
}

fun NavGraphBuilder.favoriteScreenGraph(
    openProvinceScreen: () -> Unit,
    openFavoriteWeatherScreen: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    // 别的页面打开自己
    navigation(// 有 navigation时，以 destination 为目标
        route = FavoriteDestination.route,
        startDestination = FavoriteDestination.destination // 要显示的页面，和 composable.route对应
    ) {
        composable(route = FavoriteDestination.destination) {
            FavoritesScreen(
                navigateToProvinceScreen = openProvinceScreen
            )
        }

        nestedGraphs()
    }
}