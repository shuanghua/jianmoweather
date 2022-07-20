package dev.shuanghua.ui.city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.shuanghua.core.ui.theme.topBarBackgroundColor
import dev.shuanghua.core.ui.theme.topBarForegroundColors

/**
 * 选择城市后，将城市存到数据库的收藏表
 */
@Composable
fun CityListScreen(
    provinceId: String,
    provinceName: String,
    openFavoriteScreen: () -> Unit
) {
    Box(modifier = Modifier.statusBarsPadding()) {
        Button(onClick = { openFavoriteScreen() }) {
            Text(text = "$provinceId $provinceName 的城市列表")
        }
    }
}

@Composable
fun CityScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier,
        color = topBarBackgroundColor(scrollBehavior = scrollBehavior!!)
    ) {
        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = topBarForegroundColors(),
            title = { Text(text = "省份列表") },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        )
    }
}