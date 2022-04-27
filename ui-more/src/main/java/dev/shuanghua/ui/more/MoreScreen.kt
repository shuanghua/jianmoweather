package dev.shuanghua.ui.more

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MoreScreen() {
    Surface(
        color = Color.Blue, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(text = "More")
    }
}