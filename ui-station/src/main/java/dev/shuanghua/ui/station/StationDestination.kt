package dev.shuanghua.ui.station

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.shuanghua.core.navigation.AppNavigationDestination

object StationDestination : AppNavigationDestination {
    override val route = "station_route"
    override val destination = "station_destination"
}

fun NavGraphBuilder.moreGraph() {
    composable(route = StationDestination.route) {//省份页面的地址
        StationScreen(//接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理

        )
    }
}