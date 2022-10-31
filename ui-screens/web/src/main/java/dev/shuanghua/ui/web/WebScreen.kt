package dev.shuanghua.ui.web

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
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
fun WebScreen(
    webUrl: String,
    onBackClick: () -> Unit,
//    viewModel: WebViewModel = hiltViewModel(),
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

        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
//                .height(300.dp)
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding(),
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
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
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