package dev.shuanghua.ui.screen.province

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.components.JmoTextItem
import dev.shuanghua.weather.shared.UiMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProvinceListScreen(
	onBackClick: () -> Unit,
	openCityListScreen: (String) -> Unit,
	viewModel: ProvincesViewModel = koinViewModel(),
) {
	val uiState: ProvinceUiState by viewModel.uiState.collectAsStateWithLifecycle()

	ProvinceListScreen(
		uiState = uiState,
		updateCityList = { viewModel.refresh() },
		onMessageShown = { viewModel.clearMessage(it) },
		openCityListScreen = openCityListScreen,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun ProvinceListScreen(
	uiState: ProvinceUiState,
	openCityListScreen: (String) -> Unit,
	updateCityList: () -> Unit,
	onBackClick: () -> Unit,
	onMessageShown: (Long) -> Unit,
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	val snackBarHostState = remember { SnackbarHostState() }


	val pullRefreshState = rememberPullRefreshState(
		refreshing = uiState.isLoading,
		onRefresh = updateCityList,
		refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
		refreshingOffset = 56.dp  // 当松开，转圈的位置
	)

	if (uiState.uiMessage != null) {
		val uiMessage: UiMessage = remember(uiState) { uiState.uiMessage }
		val onErrorDismissState by rememberUpdatedState(onMessageShown)

		LaunchedEffect(uiMessage, snackBarHostState) {
			snackBarHostState.showSnackbar(uiMessage.message)
			onErrorDismissState(uiMessage.id)
		}
	}

	Scaffold(
		topBar = {
			ProvinceListTopBar(
				scrollBehavior = scrollBehavior,
				onBackClick = onBackClick
			)
		}
	) { innerPadding ->
		Box(
			Modifier
				.pullRefresh(pullRefreshState)
				.fillMaxSize()
		) {
			LazyColumn(
				contentPadding = innerPadding,
				verticalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.nestedScroll(scrollBehavior.nestedScrollConnection)
					.fillMaxSize()
			) {
				items(
					items = uiState.provinces,
					key = { province -> province.name }
				) { province ->
					JmoTextItem(
						text = province.name,
						onClick = { openCityListScreen(province.name) }
					)
				}
			}

			PullRefreshIndicator(
				modifier = Modifier
					.align(Alignment.TopCenter)
					.padding(innerPadding),
				backgroundColor = MaterialTheme.colorScheme.onBackground,
				contentColor = MaterialTheme.colorScheme.background,
				scale = true,
				refreshing = uiState.isLoading,
				state = pullRefreshState,
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceListTopBar(
	scrollBehavior: TopAppBarScrollBehavior,
	onBackClick: () -> Unit,
) {
	CenterAlignedTopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = "省份列表") },
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

@Preview
@Composable
fun ProvinceItemPreview() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { }
			.padding(horizontal = 16.dp)
	) {
		Text(
			text = "北京",
			style = MaterialTheme.typography.labelMedium.copy(fontSize = 24.sp)
		)
	}
}