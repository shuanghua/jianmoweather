package dev.shuanghua.ui.screen.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.shared.UiMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesRoute(
	viewModel: FavoriteViewModel = koinViewModel(),
	openProvinceScreen: () -> Unit = {},
	openFavoriteWeatherScreen: (String, String) -> Unit
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	FavoritesScreen(
		uiState = uiState,
		onDeleteStation = { viewModel.deleteStation(it) },
		onDeleteCity = { viewModel.deleteCity(it) },
		openProvinceScreen = openProvinceScreen,
		openFavoriteWeatherScreen = openFavoriteWeatherScreen,
		onMessageShown = { viewModel.clearMessage(it) },
		onRefresh = { viewModel.refresh() }
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
	uiState: FavoriteUiState,
	modifier: Modifier = Modifier,
	onDeleteStation: (String) -> Unit,
	onDeleteCity: (String) -> Unit,
	openProvinceScreen: () -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit,
	onMessageShown: (Long) -> Unit,
	onRefresh: () -> Unit
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
			FavoriteScreenTopBar(
				scrollBehavior = scrollBehavior,
				openProvinceListScreen = openProvinceScreen
			)
		}
	) { innerPadding ->
		Box(
			modifier
				.pullRefresh(pullRefreshState)
				.fillMaxSize()
		) {
			FavoriteList(
				uiState = uiState,
				scrollBehavior = scrollBehavior,
				onDeleteStation = onDeleteStation,
				onDeleteCity = onDeleteCity,
				openFavoriteWeatherScreen = openFavoriteWeatherScreen,
				innerPadding = innerPadding,
			)
			PullRefreshIndicator(
				modifier = modifier
					.align(Alignment.TopCenter)
					.padding(innerPadding),
				backgroundColor = MaterialTheme.colorScheme.onBackground,
				contentColor = MaterialTheme.colorScheme.background,
				scale = true,
				refreshing = uiState.isLoading,
				state = pullRefreshState
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteList(
	uiState: FavoriteUiState,
	scrollBehavior: TopAppBarScrollBehavior,
	onDeleteStation: (String) -> Unit,
	onDeleteCity: (String) -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit,
	innerPadding: PaddingValues,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		contentPadding = PaddingValues(
			top = innerPadding.calculateTopPadding() + 16.dp,
			bottom = 16.dp
		),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.fillMaxSize()
			.padding(horizontal = 16.dp)
	) {
		when (uiState) {
			is FavoriteUiState.NoData -> {}
			is FavoriteUiState.HasData -> {
				if (uiState.stationWeather.isNotEmpty()) {
					item { Text(text = "站点") }
					favoriteStationList(
						stationList = uiState.stationWeather,
						onDeleteStation = onDeleteStation,
						openFavoriteWeatherScreen = openFavoriteWeatherScreen
					)
				}

				if (uiState.cityWeather.isNotEmpty()) {
					item { Text(text = "城市") }
					favoriteCityList(
						cityList = uiState.cityWeather,
						onDeleteCity = onDeleteCity,
						openFavoriteWeatherScreen = openFavoriteWeatherScreen
					)
				}
			}
		}
	}
}

private fun LazyListScope.favoriteStationList(
	stationList: List<FavoriteStation>,
	onDeleteStation: (String) -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit

) {
	stationList.forEach {
		item(key = it.stationName) {
			FavoriteStationItem(
				station = it,
				onDeleteStation = onDeleteStation,
				openFavoriteWeatherScreen = openFavoriteWeatherScreen
			)
		}
	}
}

private fun LazyListScope.favoriteCityList(
	cityList: List<FavoriteCity>,
	onDeleteCity: (String) -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit
) {
	cityList.forEach {
		item(key = it.cityId) {
			FavoriteCityItem(
				cityWeather = it,
				onDeleteCity = onDeleteCity,
				openFavoriteWeatherScreen = openFavoriteWeatherScreen
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteStationItem(
	station: FavoriteStation,
	modifier: Modifier = Modifier,
	onDeleteStation: (String) -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit
) {
	val openDeleteStationDialog = remember { mutableStateOf(false) }

	Surface(
		tonalElevation = 2.dp,
		modifier = modifier
			.height(140.dp)
			.fillMaxSize()
			.clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
			.combinedClickable(
				onClick = {
					openFavoriteWeatherScreen(station.cityId, station.stationName)
				},
				onLongClick = {
					openDeleteStationDialog.value = true
				}
			)
	) {
		Row(
			modifier = modifier.padding(
				vertical = 8.dp,
				horizontal = 16.dp
			)
		) {
			Column(
				modifier = modifier.weight(1f),
				verticalArrangement = Arrangement.Top
			) {
				Text(
					text = station.stationName,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				)
			}
			Column(
				modifier = modifier.width(150.dp),
				horizontalAlignment = Alignment.End,
				verticalArrangement = Arrangement.Center
			) {
				Text(
					textAlign = TextAlign.End,
					text = station.temperature,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				)

				AsyncImage(// coil 异步下载网络图片
					modifier = modifier
						.size(80.dp, 50.dp)
						.padding(start = 2.dp, top = 16.dp)
						.clip(shape = RoundedCornerShape(percent = 10)),
					model = station.weatherIcon,
					contentDescription = null
				)
			}

			if (openDeleteStationDialog.value) {
				DeleteDialog(
					text = "确认删除吗？站点删除后将不可恢复！",
					onDismiss = { openDeleteStationDialog.value = false },
					onDelete = {
						openDeleteStationDialog.value = false
						onDeleteStation(station.stationName)
					}
				)
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteCityItem(
	cityWeather: FavoriteCity,
	onDeleteCity: (String) -> Unit,
	openFavoriteWeatherScreen: (String, String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val openDeleteStationDialog = remember { mutableStateOf(false) }

	Surface(
		tonalElevation = 2.dp,
		modifier = modifier
			.height(140.dp)
			.fillMaxSize()
			.clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
			.combinedClickable(
				onClick = {
					openFavoriteWeatherScreen(cityWeather.cityId, "Null")
				},
				onLongClick = {
					openDeleteStationDialog.value = true
				}
			)
	) {
		Row(
			modifier = modifier
				.padding(
					vertical = 8.dp,
					horizontal = 16.dp
				)
		) {
			Column(
				modifier = modifier.weight(1f),
				verticalArrangement = Arrangement.Top
			) {
				Text(
					text = cityWeather.cityName,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				)
			}

			Column(
				modifier = modifier.width(150.dp),
				horizontalAlignment = Alignment.End,
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = cityWeather.currentT,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				)

				AsyncImage(// coil 异步下载网络图片
					modifier = modifier
						.size(80.dp, 50.dp)
						.padding(start = 2.dp, top = 8.dp)

						.clip(shape = RoundedCornerShape(percent = 10)),
					model = cityWeather.iconUrl,
					contentDescription = null
				)
			}

			if (openDeleteStationDialog.value) {
				DeleteDialog(
					text = "确认删除？",
					onDismiss = { openDeleteStationDialog.value = false },
					onDelete = {
						openDeleteStationDialog.value = false
						onDeleteCity(cityWeather.cityId)
					},
				)
			}

		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreenTopBar(
	scrollBehavior: TopAppBarScrollBehavior,
	openProvinceListScreen: () -> Unit,
) {
	TopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = "收藏") },
		actions = {
			IconButton(onClick = openProvinceListScreen) {
				Icon(
					imageVector = Icons.Filled.Add,
					contentDescription = ""
				)
			}
			// 添加更多按钮
		}
	)
}


private val defaultRoundedCornerSize = 26.dp