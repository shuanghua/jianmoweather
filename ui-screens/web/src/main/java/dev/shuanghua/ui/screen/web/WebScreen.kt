package dev.shuanghua.ui.screen.web

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
	Scaffold(
		topBar = { WebScreenTopBar(onBackClick = onBackClick) }
	) { innerPadding ->
		val state = rememberWebViewState(url = webUrl)
		WebView(
			modifier = modifier
				.fillMaxSize()
				.padding(innerPadding),
			state = state,
			onCreated = { it.settings.javaScriptEnabled = true }
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreenTopBar(
	onBackClick: () -> Unit,
) {
	CenterAlignedTopAppBar(
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.surface
		),
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