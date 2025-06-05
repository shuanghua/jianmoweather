package dev.shuanghua.ui.screen.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object SettingRoute

fun NavController.openSettings() {
	navigate(SettingRoute)
}

fun NavGraphBuilder.settingsScreen(
	onBackClick: () -> Unit,
) {
	composable<SettingRoute> {
		SettingsScreen(onBackClick = onBackClick)
	}
}