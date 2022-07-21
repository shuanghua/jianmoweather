package dev.shuanghua.ui.favorite

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.core.navigation.AppNavigationDestination

object FavoriteDestination : AppNavigationDestination {
    override val route = "favorite_route"
    override val destination = "favorite_destination"
}

fun NavGraphBuilder.favoriteGraph(
    navigateToProvinceScreen: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(//有navigation时，以 destination 为目标
        route = FavoriteDestination.route,
        startDestination = FavoriteDestination.destination // 要显示的页面，和 composable.route对应
    ) {
        composable(route = FavoriteDestination.destination) {
            FavoritesScreen(
                navigateToProvinceScreen = navigateToProvinceScreen
            )
        }
        nestedGraphs()
    }
}