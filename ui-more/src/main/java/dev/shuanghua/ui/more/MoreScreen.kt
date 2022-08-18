package dev.shuanghua.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.shuanghua.core.ui.topBarBackgroundColor
import dev.shuanghua.core.ui.topBarForegroundColors

@Composable
fun MoreScreen(
    navigateToWeb: (String) -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: MoreViewModel = hiltViewModel(),
) {

    MoreScreenUi(
        navigateToWeb = navigateToWeb,
        navigateToSettings = navigateToSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreScreenUi(
    navigateToWeb: (String) -> Unit,
    navigateToSettings: () -> Unit,
) {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            MoreScreenTopBar(
                scrollBehavior = scrollBehavior,
                navigateToSettings = navigateToSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
                .verticalScroll(rememberScrollState())
        ) {
            MoreItem(navigateToWeb = navigateToWeb)
            MoreItem(navigateToWeb = navigateToWeb)
            MoreItem(navigateToWeb = navigateToWeb)
            MoreItem(navigateToWeb = navigateToWeb)
        }
    }
}

@Composable
fun MoreItem(
    navigateToWeb: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val typhoonPathUrl = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html"
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 16.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                navigateToWeb(typhoonPathUrl)
            }
    ) {
        Text(text = "台风路径")
    }
}

@Composable
fun MoreScreenTopBar(
    navigateToSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = topBarBackgroundColor(scrollBehavior = scrollBehavior!!)
    ) {

        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = topBarForegroundColors(),
            title = { Text(text = "更多") },

            actions = {
                IconButton(onClick = navigateToSettings) {
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