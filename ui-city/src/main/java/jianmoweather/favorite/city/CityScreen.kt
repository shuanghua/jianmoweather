package jianmoweather.favorite.city

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jianmoweather.module.common_ui_compose.Screen

object CityScreen {
    fun createRoute(root: Screen) = "${root.route}/city"
    fun argsRoute(
        root: Screen,
        provinceId: String = "{provinceId}"
    ): String {
        return "${root.route}/city/$provinceId"
    }
}

@Composable
fun AnimatedVisibilityScope.CityScreen(provinceId: String, openFavoriteScreen: (String) -> Unit) {
    val cityId = "6666666"
    Box(modifier = Modifier.statusBarsPadding()) {
        Button(onClick = { openFavoriteScreen(cityId) }) {
            Text(text = "$provinceId 的城市列表")
        }
    }
}