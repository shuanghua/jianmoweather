package dev.shuanghua.ui.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavController.openSettings() {
    this.navigate("settings_route")

}

fun NavGraphBuilder.settingsScreen(
    onBackClick: () -> Unit,
) {
    composable(route = "settings_route") {
        SettingScreen(
            onBackClick = onBackClick
        )
    }
}