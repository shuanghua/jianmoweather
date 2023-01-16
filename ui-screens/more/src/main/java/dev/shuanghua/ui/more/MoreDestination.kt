package dev.shuanghua.ui.more

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.ui.core.navigation.AppNavigationDestination

object MoreDestination : AppNavigationDestination {
    override val route = "more_route"
    override val destination = "more_destination"
}

fun NavGraphBuilder.moreScreenGraph(
    openWebScreen: (String) -> Unit,
    openSettingsScreen: () -> Unit,
    nestedGraphs: () -> Unit,
) {
    navigation(
        route = MoreDestination.route,
        startDestination = MoreDestination.destination
    ) {
        composable(route = MoreDestination.destination) {
            MoreScreen(
                navigateToWeb = openWebScreen,
                navigateToSettings = openSettingsScreen,
            )
        }
        nestedGraphs()
    }
}