package dev.shuanghua.ui.district

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.shuanghua.core.navigation.AppNavigationDestination
import dev.shuanghua.ui.district.DistrictDestination.cityIdArg
import dev.shuanghua.ui.district.DistrictDestination.obtIdArg

object DistrictDestination : AppNavigationDestination {
    override val route = "district_route"
    override val destination = "district_destination"

    const val cityIdArg = "cityId"
    const val obtIdArg = "obtId"
}

fun NavGraphBuilder.districtGraph(
    onBackClick: () -> Unit,
    navigateToStationScreen: (String) -> Unit
) {
    composable(route = DistrictDestination.route + "/{cityId}/{obtId}") { // 省份页面的地址
        DistrictScreen(  // 接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
            onBackClick = onBackClick,
            navigateToStationScreen = navigateToStationScreen
        )
    }
}