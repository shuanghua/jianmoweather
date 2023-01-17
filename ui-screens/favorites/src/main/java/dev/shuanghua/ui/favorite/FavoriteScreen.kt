package dev.shuanghua.ui.favorite

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.shared.UiMessage

@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    openProvinceScreen: () -> Unit = {},
    openFavoriteWeatherScreen: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        uiState = uiState,
        deleteStation = { viewModel.deleteStation(it) },
        deleteCity = { viewModel.deleteCity(it) },
        openProvinceScreen = openProvinceScreen,
        openFavoriteWeatherScreen = openFavoriteWeatherScreen,
        onMessageShown = { viewModel.clearMessage(it) },
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun FavoritesScreen(
    uiState: FavoriteUiState,
    modifier: Modifier = Modifier,
    deleteStation: (String) -> Unit,
    deleteCity: (String) -> Unit,
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
                deleteStation = deleteStation,
                deleteCity = deleteCity,
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
    deleteStation: (String) -> Unit,
    deleteCity: (String) -> Unit,
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
                    FavoriteStationList(
                        stationList = uiState.stationWeather,
                        deleteStation = deleteStation,
                        openFavoriteWeatherScreen = openFavoriteWeatherScreen
                    )
                }

                if (uiState.cityWeather.isNotEmpty()) {
                    item { Text(text = "城市") }
                    FavoriteCityList(
                        cityList = uiState.cityWeather,
                        deleteCity = deleteCity,
                        openFavoriteWeatherScreen = openFavoriteWeatherScreen
                    )
                }
            }
        }
    }
}

private fun LazyListScope.FavoriteStationList(
    stationList: List<FavoriteStation>,
    deleteStation: (String) -> Unit,
    openFavoriteWeatherScreen: (String, String) -> Unit

) {
    stationList.forEach {
        item(key = it.stationName) {
            FavoriteStationItem(
                station = it,
                onDeleteStation = deleteStation,
                openFavoriteWeatherScreen = openFavoriteWeatherScreen
            )
        }
    }
}

private fun LazyListScope.FavoriteCityList(
    cityList: List<FavoriteCity>,
    deleteCity: (String) -> Unit,
    openFavoriteWeatherScreen: (String, String) -> Unit
) {
    cityList.forEach {
        item(key = it.cityId) {
            FavoriteCityItem(
                cityWeather = it,
                deleteCity = deleteCity,
                openFavoriteWeatherScreen = openFavoriteWeatherScreen
            )
        }
    }
}

@Composable
fun FavoriteStationItem(
    station: FavoriteStation,
    modifier: Modifier = Modifier,
    onDeleteStation: (String) -> Unit,
    openFavoriteWeatherScreen: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }
    val openDeleteStationDialog = remember { mutableStateOf(false) }

    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .height(120.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset: Offset ->
                        expanded = true
                        menuOffset = offset
                    },
                    onTap = { openFavoriteWeatherScreen(station.cityId, station.stationName) }
                )
            }
    ) {
        Row(modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
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
                        .padding(start = 2.dp)
                        .clip(shape = RoundedCornerShape(percent = 10)),
                    model = station.weatherIcon,
//                  contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

            val offsetX = with(LocalDensity.current) { menuOffset.x.toDp() }
            val offsetY = with(LocalDensity.current) { menuOffset.y.toDp() }
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset(offsetX, offsetY - 100.dp),
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "删除") },
                    onClick = {
                        expanded = false
                        openDeleteStationDialog.value = true
                    })
            }
            if (openDeleteStationDialog.value) {
                DeleteStationDialog(
                    onDismiss = { openDeleteStationDialog.value = false },
                    onDeleteStation = {
                        openDeleteStationDialog.value = false
                        onDeleteStation(station.stationName)
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteCityItem(
    cityWeather: FavoriteCity,
    deleteCity: (String) -> Unit,
    openFavoriteWeatherScreen: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }

    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .height(120.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset: Offset ->
                        expanded = true
                        menuOffset = offset
                    },
                    onTap = {
                        openFavoriteWeatherScreen(cityWeather.cityId, cityWeather.cityName)
                    }
                )
            }
    ) {
        Row(modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
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
                    text = cityWeather.maxT,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                AsyncImage(// coil 异步下载网络图片
                    modifier = modifier
                        .size(80.dp, 50.dp)
                        .padding(start = 2.dp)
                        .clip(shape = RoundedCornerShape(percent = 10)),
                    model = cityWeather.iconUrl,
//                  contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

            val offsetX = with(LocalDensity.current) { menuOffset.x.toDp() }
            val offsetY = with(LocalDensity.current) { menuOffset.y.toDp() }
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset(offsetX, offsetY - 100.dp),
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "删除") },
                    onClick = {
                        expanded = false
                        deleteCity(cityWeather.cityId)
                    })
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