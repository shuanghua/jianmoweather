package dev.shuanghua.ui.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.components.AlarmIconList
import dev.shuanghua.ui.core.components.ConditionList
import dev.shuanghua.ui.core.components.ExponentItems
import dev.shuanghua.ui.core.components.HorizontalListTitle
import dev.shuanghua.ui.core.components.MainTemperature
import dev.shuanghua.ui.core.components.OneDayList
import dev.shuanghua.ui.core.components.OneHourList
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.ifNullToValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel


@ExperimentalCoroutinesApi
@Composable
internal fun WeatherRoute(
	openAirDetails: (String) -> Unit,
	navigateToDistrictScreen: (String, String) -> Unit,
	viewModel: WeatherViewModel = koinViewModel(),
) {

	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	WeatherScreen(
		uiState = uiState,
		openAirDetailsScreen = openAirDetails,
		updateWeather = { viewModel.refresh() },
		navigateToDistrictScreen = navigateToDistrictScreen,
		addToFavorite = { viewModel.addStationToFavorite() },
		onClearUiMessage = { viewModel.clearMessage(it) }
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun WeatherScreen(
	uiState: WeatherUiState,
	modifier: Modifier = Modifier,
	navigateToDistrictScreen: (String, String) -> Unit,
	openAirDetailsScreen: (String) -> Unit,
	addToFavorite: () -> Unit,
	updateWeather: () -> Unit,
	onClearUiMessage: (id: Long) -> Unit,
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

	val pullRefreshState = rememberPullRefreshState(
		refreshing = uiState.isLoading,
		onRefresh = updateWeather,
		refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
		refreshingOffset = 56.dp  // 当松开，转圈的位置
	)

	if (uiState.uiMessage != null) {
		val uiMessage: UiMessage = remember(uiState) { uiState.uiMessage!! }
		val onClearUiMessageState by rememberUpdatedState(onClearUiMessage)
		LaunchedEffect(uiMessage, snackBarHostState) {
			snackBarHostState.showSnackbar(uiMessage.message)
			onClearUiMessageState(uiMessage.id)
		}
	}

	Scaffold(
		snackbarHost = { SnackbarHost(snackBarHostState) },
		topBar = {
			when (uiState) {
				is WeatherUiState.NoData -> {
					WeatherScreenTopBar(
						title = "",
						scrollBehavior = scrollBehavior,
						addToFavorite = addToFavorite
					)
				}

				is WeatherUiState.HasData -> {
					WeatherScreenTopBar(
						title = uiState.weather.cityName.ifNullToValue(),
						scrollBehavior = scrollBehavior,
						addToFavorite = addToFavorite
					)
				}
			}
		},
	) { innerPadding ->
		Box(
			modifier
				.pullRefresh(pullRefreshState)
				.fillMaxSize()
		) {
			WeatherList(
				uiState = uiState,
				innerPadding = innerPadding,
				scrollBehavior = scrollBehavior,
				navigateToDistrictScreen = navigateToDistrictScreen,
				openAirDetailsScreen = openAirDetailsScreen
			)
			PullRefreshIndicator(
				modifier = modifier
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
fun WeatherList(
	uiState: WeatherUiState,
	innerPadding: PaddingValues,
	scrollBehavior: TopAppBarScrollBehavior,
	navigateToDistrictScreen: (String, String) -> Unit,
	openAirDetailsScreen: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		contentPadding = PaddingValues(
			top = innerPadding.calculateTopPadding() + 16.dp,
			bottom = 16.dp
		),
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.fillMaxSize()
	) {
		when (uiState) {
			is WeatherUiState.NoData -> {}
			is WeatherUiState.HasData -> {

				item { AlarmIconList(uiState.weather.alarmIcons) }

				item {
					MainTemperature(
						weather = uiState.weather,
						openDistrictListScreen = navigateToDistrictScreen,
						openAirDetailsScreen = openAirDetailsScreen
					)
				}

				if (uiState.weather.oneHours.isNotEmpty()) {
					item { HorizontalListTitle("每时天气") }
					item { OneHourList(oneHours = uiState.weather.oneHours) }
				}

				if (uiState.weather.oneDays.isNotEmpty()) {
					item { HorizontalListTitle("每日天气") }
					item { OneDayList(oneDays = uiState.weather.oneDays) }
				}

				if (uiState.weather.conditions.isNotEmpty()) {
					item { ConditionList(conditions = uiState.weather.conditions) }
				}

				if (uiState.weather.exponents.isNotEmpty()) {
					item { ExponentItems(exponents = uiState.weather.exponents) }
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenTopBar(
	title: String = "城市",
	scrollBehavior: TopAppBarScrollBehavior,
	addToFavorite: () -> Unit,
) {
	// 从上层到下层: status图标，Status背景色, TopBar ,  Content
	CenterAlignedTopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
		actions = {
			var expanded by remember { mutableStateOf(false) }
			IconButton(onClick = { expanded = true }) {
				Icon(Icons.Default.MoreVert, contentDescription = "更多选项")
			}

			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				DropdownMenuItem(
					text = { Text(text = "添加到收藏") },
					onClick = {
						addToFavorite()
						expanded = false
					})
			}
		}
	)
}