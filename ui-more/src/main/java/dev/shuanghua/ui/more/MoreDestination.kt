package dev.shuanghua.ui.more

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.core.navigation.AppNavigationDestination

object MoreDestination : AppNavigationDestination {
    override val route = "more_route"
    override val destination = "more_destination"
}

fun NavGraphBuilder.moreScreenGraph(
    navigateToWeb: (String) -> Unit,
    navigateToSettings: () -> Unit,
    nestedGraphs: () -> Unit,
) {
    navigation(
        route = MoreDestination.route,
        startDestination = MoreDestination.destination
    ) {
        composable(route = MoreDestination.destination) {
            MoreScreen(
                navigateToWeb = navigateToWeb,
                navigateToSettings = navigateToSettings,
            )
        }
        nestedGraphs()
    }
    // TODO 需要处理 url 和 route 格式冲突问题
}