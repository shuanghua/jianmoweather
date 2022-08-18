package dev.shuanghua.ui.province

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.shuanghua.core.navigation.AppNavigationDestination

object ProvinceDestination : AppNavigationDestination {
    override val route = "province_route"
    override val destination = "province_destination"
}

fun NavGraphBuilder.provinceScreenGraph(
    onBackClick: () -> Unit,
    navigateToCityScreen: (String, String) -> Unit,
) {
    composable(route = ProvinceDestination.route) {//省份页面的地址
        ProvinceScreen(//接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
            onBackClick = onBackClick,
            navigateToCityScreen = navigateToCityScreen,
        )
    }
}