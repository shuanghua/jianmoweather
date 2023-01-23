package dev.shuanghua.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.compose.components.AlarmIconList
import dev.shuanghua.ui.core.compose.components.ConditionList
import dev.shuanghua.ui.core.compose.components.ExponentItems
import dev.shuanghua.ui.core.compose.components.HorizontalListTitle
import dev.shuanghua.ui.core.compose.components.MainTemperature
import dev.shuanghua.ui.core.compose.components.OneDayList
import dev.shuanghua.ui.core.compose.components.OneHourList
import dev.shuanghua.weather.shared.ifNullToValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@Composable
fun WeatherScreen(
    openAirDetails: (String) -> Unit,
    navigateToDistrictScreen: (String, String) -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherScreen(
        uiState = uiState,
        openAirDetails = openAirDetails,
        updataWeather = { viewModel.refresh() },
        navigateToDistrictScreen = navigateToDistrictScreen,
        addToFavorite = { viewModel.addStationToFavoriteList() },
        onMessageShown = { viewModel.clearMessage(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
    navigateToDistrictScreen: (String, String) -> Unit,
    openAirDetails: (String) -> Unit,
    addToFavorite: () -> Unit,
    updataWeather: () -> Unit,
    onMessageShown: (id: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = updataWeather,
        refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
        refreshingOffset = 56.dp  // 当松开，转圈的位置
    )

    uiState.errorMessage?.let { errorMessage ->
        scope.launch {
            snackBarHostState.showSnackbar(
                message = errorMessage.message,
                duration = SnackbarDuration.Short
            )
            onMessageShown(errorMessage.id)
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
                openAirDetailsScreen = openAirDetails
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
internal fun WeatherList(
    uiState: WeatherUiState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToDistrictScreen: (String, String) -> Unit,
    openAirDetailsScreen: (String) -> Unit,
    modifier: Modifier = Modifier
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
                        openAirDetailsWebScreen = openAirDetailsScreen
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
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
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