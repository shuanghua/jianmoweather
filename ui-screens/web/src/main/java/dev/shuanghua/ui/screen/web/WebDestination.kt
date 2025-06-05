package dev.shuanghua.ui.screen.web

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable


@Serializable
data class WebRoute(val url: String)

fun NavController.openWeb(url: String) {
	navigate(WebRoute(url))
}

fun NavGraphBuilder.webScreen(
	onBackClick: () -> Unit,
) {
	composable<WebRoute> { backStackEntry ->  // 在这里直接取出值，是为了可以直接将值传递到 Screen 中使用
		backStackEntry.arguments?.getString("url").let {
			WebRoute(
				webUrl = Uri.decode(it),
				onBackClick = onBackClick
			)
		}
	}
}