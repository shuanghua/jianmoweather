package dev.shuanghua.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            MoreScreenTopBar(
                scrollBehavior = scrollBehavior,
                navigateToSettings = navigateToSettings
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = innerPadding.calculateTopPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            item {
                    MoreItem(
                        title = "台风路径-深圳APP",
                        url = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html",
                        navigateToWeb = navigateToWeb
                    )
                }

            item {
                MoreItem(
                    title = "深圳台风网",
                    url = "http://tf.121.com.cn/wap.htm",
                    navigateToWeb = navigateToWeb
                )
            }

            item {
                MoreItem(
                    title = "台风路径-IstrongCloud",
                    url = "https://tf.istrongcloud.com/",
                    navigateToWeb = navigateToWeb
                )
            }





        }
    }
}
//MoreItem(navigateToWeb = navigateToWeb)

@Composable
fun MoreItem(
    title: String,
    url: String,
    navigateToWeb: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 16.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable { navigateToWeb(url) }
    ) {
        Text(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreenTopBar(
    navigateToSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
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