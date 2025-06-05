package dev.shuanghua.ui.screen.favorites.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.SnackbarHost
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
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavoritesDetailScreen(
	openAirDetailsWebScreen: (String) -> Unit,
	onBackClick: () -> Unit,
	viewModel: FavoritesDetailViewModel = koinViewModel(),
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FavoritesWeatherScreen(
		uiState = uiState,
		onBackClick = onBackClick,
		onRefresh = { viewModel.refresh() },
		onMessageShown = { viewModel.clearMessage(it) },
		openAirDetailsWebScreen = openAirDetailsWebScreen
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun FavoritesWeatherScreen(
	uiState: WeatherUiState,
	modifier: Modifier = Modifier,
	onRefresh: () -> Unit,
	onMessageShown: (id: Long) -> Unit,
	onBackClick: () -> Unit,
	openAirDetailsWebScreen: (String) -> Unit,
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	val snackBarHostState = remember { SnackbarHostState() }
	val pullRefreshState = rememberPullRefreshState(
		refreshing = uiState.isLoading,
		onRefresh = onRefresh,
		refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
		refreshingOffset = 56.dp  // 当松开，转圈的位置
	)

	if (uiState.uiMessage.isNotEmpty()) {
		val errorMessage: UiMessage = remember(uiState) { uiState.uiMessage[0] }
		val onErrorDismissState by rememberUpdatedState(onMessageShown)
		LaunchedEffect(errorMessage, snackBarHostState) {
			snackBarHostState.showSnackbar(errorMessage.message)
			onErrorDismissState(errorMessage.id)
		}
	}

	Scaffold(
		snackbarHost = { SnackbarHost(snackBarHostState) },
		topBar = {
			when (uiState) {
				is WeatherUiState.NoData -> {
					FavoritesWeatherScreenTopBar(
						title = "Loading..",
						scrollBehavior = scrollBehavior,
						onBackClick = onBackClick
					)
				}

				is WeatherUiState.HasData -> {
					FavoritesWeatherScreenTopBar(
						title = uiState.weather.cityName.ifEmpty { "" },
						scrollBehavior = scrollBehavior,
						onBackClick = onBackClick
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
			FavoritesWeatherList(
				uiState = uiState,
				innerPadding = innerPadding,
				scrollBehavior = scrollBehavior,
				openAirDetailsWebScreen = openAirDetailsWebScreen
			)
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
internal fun FavoritesWeatherList(
	modifier: Modifier = Modifier,
	uiState: WeatherUiState,
	innerPadding: PaddingValues,
	scrollBehavior: TopAppBarScrollBehavior,
	openDistrictListScreen: (String, String) -> Unit = { _, _ -> },
	openAirDetailsWebScreen: (String) -> Unit,
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
						openDistrictListScreen = openDistrictListScreen,
						openAirDetailsScreen = openAirDetailsWebScreen
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
fun FavoritesWeatherScreenTopBar(
	title: String,
	onBackClick: () -> Unit,
	scrollBehavior: TopAppBarScrollBehavior,
	modifier: Modifier = Modifier,
) {
	// 从上层到下层: status图标，Status背景色, TopBar ,  Content
	CenterAlignedTopAppBar(
		scrollBehavior = scrollBehavior,
		modifier = modifier,
		navigationIcon = {
			IconButton(onClick = onBackClick) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "返回"
				)
			}
		},
		title = {
			Text(
				text = title,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	)
}