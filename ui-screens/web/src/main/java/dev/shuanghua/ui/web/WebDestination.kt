package dev.shuanghua.ui.web

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import dev.shuanghua.core.navigation.AppNavigationDestination
import dev.shuanghua.ui.web.WebDestination.urlArg

object WebDestination : AppNavigationDestination {
    override val route = "web_route"
    override val destination = "web_destination"
    const val urlArg = "url"
}

fun NavGraphBuilder.webScreenGraph(
    onBackClick: () -> Unit
) {
    navigation(
        route = WebDestination.route + "/{$urlArg}",
        arguments = listOf(navArgument(urlArg) { type = NavType.StringType }),
        startDestination = WebDestination.destination
    ) {
        composable(route = WebDestination.destination) {
            WebScreen(onBackClick = onBackClick)
        }
    }
}