package dev.shuanghua.ui.more

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.core.ui.topBarBackgroundColor
import dev.shuanghua.core.ui.topBarForegroundColors
import dev.shuanghua.module.ui.compose.ThemeModeDialog

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel(),
) {
    val themeModeUiState by viewModel.themeModeState.collectAsStateWithLifecycle()

    MoreScreen(
        currentThemeMode = themeModeUiState.tm,
        onThemeModeChange = { viewModel.setThemeMode(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    currentThemeMode: Int,
    onThemeModeChange: (String) -> Unit,
) {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            MoreScreenTopBar(
                scrollBehavior = scrollBehavior,
                currentThemeMode = currentThemeMode,
                onThemeModeChange = onThemeModeChange
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
fun MoreScreenTopBar(
    modifier: Modifier = Modifier,
    currentThemeMode: Int,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onThemeModeChange: (String) -> Unit,
) {
    Surface(
        modifier = Modifier,
        color = topBarBackgroundColor(scrollBehavior = scrollBehavior!!)
    ) {
        var dialogShow by remember { mutableStateOf(false) }
        if (dialogShow) {
            ThemeModeDialog(
                currentThemeMode = currentThemeMode,
                onDismiss = { dialogShow = false },
                onThemeModeChange = onThemeModeChange
            )
        }

        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = topBarForegroundColors(),
            title = { Text(text = "更多") },

            actions = {
                IconButton(onClick = { dialogShow = true }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "设置"
                    )
                }
                // 添加更多按钮
            }
        )
    }
}