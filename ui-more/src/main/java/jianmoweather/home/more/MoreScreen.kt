package jianmoweather.home.more

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import jianmoweather.module.common_ui_compose.Screen

object MoreScreen {
    fun createRoute() = Screen.More.route
}


@Preview
@Composable
fun MoreScreen() {
    Surface(color = Color.Blue, modifier = Modifier.statusBarsPadding()) {
        Text(text = "More")
    }
}