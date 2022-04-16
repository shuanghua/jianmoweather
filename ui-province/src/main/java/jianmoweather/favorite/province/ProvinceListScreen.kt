package jianmoweather.favorite.province

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProvinceListScreen(openCityScreen: (String) -> Unit) {
    val provinceId = "1234878"
    Box(modifier = Modifier.statusBarsPadding()) {
        Button(onClick = { openCityScreen(provinceId) }) {
            Text(text = "传值打开到 City 界面")
        }
    }
}