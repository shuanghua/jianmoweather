package dev.shuanghua.ui.station

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.shuanghua.core.ui.theme.topBarBackgroundColor
import dev.shuanghua.core.ui.theme.topBarForegroundColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationScreen() {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            StationTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = {}
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
fun StationTopBar(
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