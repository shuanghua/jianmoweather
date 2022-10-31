package dev.shuanghua.ui.web

import android.net.Uri
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
    onBackClick: () -> Unit,
) {
    navigation(
        route = WebDestination.route + "/{$urlArg}",  //  web_route/{url}
        arguments = listOf(navArgument(urlArg) {
            type = NavType.StringType
            defaultValue = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html"
        }),
        startDestination = WebDestination.destination
    ) {
        composable(route = WebDestination.destination) { backStackEntry ->
            backStackEntry.arguments?.getString(urlArg)
                ?.let { WebScreen(webUrl = Uri.decode(it), onBackClick = onBackClick) }
        }
    }
}