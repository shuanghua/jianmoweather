package dev.shuanghua.ui.screen.district

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data class DistrictRoute(val cityId: String, val stationName: String)


fun NavController.openDistrictList(
	cityId: String,
	stationName: String,
) {
//	this.navigate("district_route/$cityId/$stationName")
	navigate(DistrictRoute(cityId, stationName))
}

fun NavGraphBuilder.districtScreen(
	onBackClick: () -> Unit,
	openStationList: (String) -> Unit,
) {
	composable<DistrictRoute> { // 省份页面的地址
		DistrictsRoute(
			onBackClick = onBackClick,
			navigateToStationScreen = openStationList
		)
	}
}