package dev.shuanghua.ui.screen.district

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val cityIdArg = "cityId"
internal const val stationNameArg = "stationNameArg"

fun NavController.openDistrictList(
    cityId: String,
    stationName: String
) {
    this.navigate("district_route/$cityId/$stationName")
}

fun NavGraphBuilder.districtScreen(
    onBackClick: () -> Unit,
    openStationList: (String) -> Unit,
) {
    composable(route = "district_route/{$cityIdArg}/{$stationNameArg}") { // 省份页面的地址
        DistrictListRoute(
            onBackClick = onBackClick,
            navigateToStationScreen = openStationList
        )
    }
}