package dev.shuanghua.ui.screen.web

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


internal val urlArg = "url"

fun NavController.openWeb(url: String) {
    this.navigate(route = "web_route/${Uri.encode(url)}")
}

fun NavGraphBuilder.webScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "web_route/{$urlArg}",
        arguments = listOf(navArgument(urlArg) {
            type = NavType.StringType
            defaultValue = "https://github.com/shuanghua"
        })
    ) { backStackEntry ->  // 在这里直接取出值，是为了可以直接将值传递到 Screen 中使用
        backStackEntry.arguments?.getString(urlArg).let {
            WebRoute(
                webUrl = Uri.decode(it),
                onBackClick = onBackClick
            )
        }
    }
}