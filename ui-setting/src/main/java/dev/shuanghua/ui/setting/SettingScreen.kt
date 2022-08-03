package dev.shuanghua.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.shuanghua.core.ui.topBarBackgroundColor
import dev.shuanghua.core.ui.topBarForegroundColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            SettingTopBar(
                scrollBehavior = scrollBehavior,
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