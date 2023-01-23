package dev.shuanghua.ui.station

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val districtNameArg = "districtName"

fun NavController.openStationList(districtName: String) {
    this.navigate("station_route/$districtName")
}

fun NavGraphBuilder.stationScreen(
    onBackClick: () -> Unit,
    openWeatherScreen: () -> Unit,
) {
    composable(route = "station_route/{$districtNameArg}") {//省份页面的地址
        StationListScreen(  //接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
            onBackClick = onBackClick,
            openWeatherScreen = openWeatherScreen
        )
    }
}