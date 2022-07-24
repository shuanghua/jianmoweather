package dev.shuanghua.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.shuanghua.core.ui.theme.topBarBackgroundColor
import dev.shuanghua.core.ui.theme.topBarForegroundColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            SettingTopBar(
                scrollBehavior = scrollBehavior,
                navigateToSettingScreen = {}
            )
        }
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = ""
        )
    }
}

@Composable
fun SettingTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateToSettingScreen: () -> Unit
) {
    Surface(
        modifier = Modifier,
        color = topBarBackgroundColor(scrollBehavior = scrollBehavior!!)
    ) {
        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = topBarForegroundColors(),
            title = { Text(text = "站点") },
//            navigationIcon = {
//                IconButton(onClick = { navigateToSettingScreen() }) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = "返回"
//                    )
//                }
//            }
        )
    }
}