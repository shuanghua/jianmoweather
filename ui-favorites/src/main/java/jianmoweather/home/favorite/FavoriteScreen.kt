package jianmoweather.home.favorite

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalAnimationApi
fun FavoritesScreen(openProvinceScreen: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Button(onClick = { openProvinceScreen() }) {
            Text(text = "打开省份页面")
        }
        AnimatedCircle(
            modifier = Modifier
                .height(300.dp)
                .align(Alignment.Center)
                .fillMaxWidth()
        )
        //AnimatingBox(BoxState.Collapsed)
    }
}

@Preview(device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun PreviewFavorite() {
    Box(modifier = Modifier.statusBarsPadding()) {
        AnimatedCircle(
            modifier = Modifier
                .height(300.dp)
                .align(Alignment.Center)
                .fillMaxWidth()
        )
    }
}