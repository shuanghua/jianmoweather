package dev.shuanghua.ui.screen.web

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebRoute(
    webUrl: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
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
        val state = rememberWebViewState(url = webUrl)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                WebView(
                    state = state,
                    onCreated = { it.settings.javaScriptEnabled = true }
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreenTopBar(
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior,
        title = { Text(text = "Web") },
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