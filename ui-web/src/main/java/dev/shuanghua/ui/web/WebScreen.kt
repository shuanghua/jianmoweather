package dev.shuanghua.ui.web

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(
    onBackClick: () -> Unit,
    viewModel: WebViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            WebScreenTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        val state = rememberWebViewState(
            url = "http://szqxapp1.121.com.cn:80/phone/app/webPage/typhoon/typhoon.html"
        )

        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
//                .height(300.dp)
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
                .clip(shape = RoundedCornerShape(26.dp))
        ) {
            WebView(
                state = state,
                onCreated = { it.settings.javaScriptEnabled = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreenTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
) {

    TopAppBar(
        modifier = modifier.statusBarsPadding(),
        scrollBehavior = scrollBehavior,
        title = { Text(text = "更多") },
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