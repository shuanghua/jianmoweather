package dev.shuanghua.ui.screen.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WeatherRoute

fun NavGraphBuilder.weatherScreen(
	openAirDetails: (String) -> Unit,
	openDistrictList: (String, String) -> Unit,
) {
	composable<WeatherRoute> {
		WeatherScreen(
			openAirDetails = openAirDetails,
			navigateToDistrictScreen = openDistrictList
		)
	}
}