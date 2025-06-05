package dev.shuanghua.ui.screen.station

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data class StationRoute(val districtName: String)

fun NavController.openStationList(districtName: String) {
    navigate(StationRoute(districtName))
}

fun NavGraphBuilder.stationScreen(
    onBackClick: () -> Unit,
    openWeatherScreen: () -> Unit,
) {
    composable<StationRoute> {//省份页面的地址
        StationListRoute(  //接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
            onBackClick = onBackClick,
            openWeatherScreen = openWeatherScreen
        )
    }
}