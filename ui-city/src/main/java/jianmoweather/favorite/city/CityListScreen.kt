package jianmoweather.favorite.city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CityListScreen(provinceId: String, openFavoriteScreen: (String) -> Unit) {
    val cityId = "6666666"
    Box(modifier = Modifier.statusBarsPadding()) {
        Button(onClick = { openFavoriteScreen(cityId) }) {
            Text(text = "$provinceId 的城市列表")
        }
    }
}