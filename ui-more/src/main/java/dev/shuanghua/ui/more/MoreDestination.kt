package dev.shuanghua.ui.more

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.shuanghua.core.navigation.AppNavigationDestination

object MoreDestination : AppNavigationDestination {
    override val route = "more_route"
    override val destination = "more_destination"
}

fun NavGraphBuilder.moreGraph() {
    composable(route = MoreDestination.route) {//省份页面的地址
        MoreScreen(//接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理

        )
    }
}