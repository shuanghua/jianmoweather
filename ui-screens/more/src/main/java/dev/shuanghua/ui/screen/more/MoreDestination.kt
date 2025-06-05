package dev.shuanghua.ui.screen.more

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object MoreRoute

fun NavGraphBuilder.moreScreen(
	openWebLink: (String) -> Unit,
	openSettings: () -> Unit,
) {
	composable<MoreRoute> {
		MoreScreen(
			navigateToWeb = openWebLink,
			navigateToSettings = openSettings,
		)
	}
}