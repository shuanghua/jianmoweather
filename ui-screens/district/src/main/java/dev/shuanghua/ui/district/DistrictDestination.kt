package dev.shuanghua.ui.district

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val cityIdArg = "cityId"
internal const val obtIdArg = "obtIdArg"

fun NavController.openDistrictList(
    cityId: String,
    obtId: String
) {
    this.navigate("district_route/$cityId/$obtId")
}

fun NavGraphBuilder.districtScreen(
    onBackClick: () -> Unit,
    openStationList: (String) -> Unit,
) {
    composable(route = "district_route/{$cityIdArg}/{$obtIdArg}") { // 省份页面的地址
        DistrictScreen(
            onBackClick = onBackClick,
            navigateToStationScreen = openStationList
        )
    }
}