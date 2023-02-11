package dev.shuanghua.ui.screen.more

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val moreNavigation = "more_navigation"
const val moreRoute = "more_route"

fun NavGraphBuilder.moreScreen(
    openWebLink: (String) -> Unit,
    openSettings: () -> Unit,
    nestedGraphs: () -> Unit,
) {
    navigation(
        route = moreNavigation,
        startDestination = moreRoute
    ) {
        composable(route = moreRoute) {
            MoreRoute(
                navigateToWeb = openWebLink,
                navigateToSettings = openSettings,
            )
        }
        nestedGraphs()
    }
}