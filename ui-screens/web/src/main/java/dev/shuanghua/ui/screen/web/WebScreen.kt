package dev.shuanghua.ui.screen.web

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
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
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "返回"
				)
			}
		}
	)
}