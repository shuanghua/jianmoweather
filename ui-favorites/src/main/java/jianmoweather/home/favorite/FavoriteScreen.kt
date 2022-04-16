package jianmoweather.home.favorite

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jianmoweather.module.common_ui_compose.Screen


object FavoriteScreen {
    fun route() = Screen.Favorite.route
}

@Composable
@ExperimentalAnimationApi
fun FavoritesScreen(openProvinceScreen: () -> Unit) {
    Box(modifier = Modifier.statusBarsPadding()) {
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