package dev.shuanghua.ui.screen.district

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.components.JmoTextItem
import dev.shuanghua.weather.data.android.model.District
import org.koin.androidx.compose.koinViewModel

@Composable
fun DistrictsRoute(
	onBackClick: () -> Unit,
	navigateToStationScreen: (String) -> Unit,
	viewModel: DistrictListViewModel = koinViewModel(),
) {
	val uiState: DistrictListUiState by viewModel.uiState.collectAsStateWithLifecycle()

	DistrictsScreen(
		uiState = uiState,
		updateDistrictList = { viewModel.refresh() },
		onBackClick = onBackClick,
		openStationScreen = navigateToStationScreen
	)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun DistrictsScreen(
	uiState: DistrictListUiState,
	updateDistrictList: () -> Unit,
	openStationScreen: (String) -> Unit,
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	val pullRefreshState = rememberPullRefreshState(
		refreshing = uiState.isLoading,
		onRefresh = updateDistrictList,
		refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
		refreshingOffset = 56.dp  // 当松开，转圈的位置
	)

	Scaffold(
		topBar = {
			DistrictTopBar(
				scrollBehavior = scrollBehavior,
				onBackClick = onBackClick
			)
		}
	) { innerPadding ->

		Box(
			modifier = modifier
				.pullRefresh(pullRefreshState)
				.fillMaxSize()
		) {

			DistrictList(
				districtList = uiState.districtList,
				scrollBehavior = scrollBehavior,
				innerPadding = innerPadding,
				openStationScreen = openStationScreen
			)

			PullRefreshIndicator(
				modifier = Modifier
					.align(Alignment.TopCenter)
					.padding(innerPadding),
				backgroundColor = MaterialTheme.colorScheme.onBackground,
				contentColor = MaterialTheme.colorScheme.background,
				scale = true,
				refreshing = uiState.isLoading,
				state = pullRefreshState
			)

			if (uiState.districtList.isEmpty()) {
				Text(
					text = "Loading..",
					style = MaterialTheme.typography.headlineSmall,
					modifier = modifier
						.align(Alignment.Center),
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictList(
	districtList: List<District>,
	scrollBehavior: TopAppBarScrollBehavior,
	innerPadding: PaddingValues,
	openStationScreen: (String) -> Unit,
	modifier: Modifier = Modifier

) {
	LazyColumn(
		contentPadding = innerPadding,
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.fillMaxSize()
	) {
		items(
			items = districtList,
			key = { district -> district.name }
		) { district ->
			JmoTextItem(
				text = district.name,
				onClick = { openStationScreen(district.name) }
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictTopBar(
	scrollBehavior: TopAppBarScrollBehavior,
	onBackClick: () -> Unit,
) {
	CenterAlignedTopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = "区县") },
		navigationIcon = {
			IconButton(onClick = onBackClick) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "返回"
				)
			}
		}
	)
}
