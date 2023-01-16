package dev.shuanghua.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.shuanghua.module.ui.compose.DescriptionDialog
import dev.shuanghua.module.ui.compose.JianMoLazyRow
import dev.shuanghua.weather.data.android.model.AlarmIcon
import dev.shuanghua.weather.data.android.model.Condition
import dev.shuanghua.weather.data.android.model.Exponent
import dev.shuanghua.weather.data.android.model.OneDay
import dev.shuanghua.weather.data.android.model.OneHour
import dev.shuanghua.weather.data.android.model.Weather
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
        onRefresh = { viewModel.refresh() },
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
    onRefresh: () -> Unit,
    onMessageShown: (id: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefresh,
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
                openAirDetails = openAirDetails
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
    openAirDetails: (String) -> Unit,
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

                item {
                    if (uiState.weather.airQuality.isNotBlank()) {
                        AirQuality(
                            cityId = uiState.weather.cityId,
                            airQuality = uiState.weather.airQuality,
                            airQualityIcon = uiState.weather.airQualityIcon,
                            openAirDetails = openAirDetails
                        )
                    }
                }

                item {
                    AlarmImageList(uiState.weather.alarmIcons)
                }

                item {
                    Temperature(
                        weather = uiState.weather,
                        navigateToDistrictScreen = navigateToDistrictScreen,
                    )
                }

                if (uiState.weather.oneHours.isNotEmpty()) {
                    item { ListTitleItem("每时天气") }
                    item { OneHourList(oneHours = uiState.weather.oneHours) }
                }

                if (uiState.weather.oneDays.isNotEmpty()) {
                    item { ListTitleItem("每日天气") }
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


@Composable
fun AirQuality(
    cityId: String,
    airQuality: String,
    airQualityIcon: String,
    openAirDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        OutlinedButton(
            onClick = { openAirDetails(cityId) },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            AsyncImage(// coil 异步下载网络图片
                modifier = modifier.size(24.dp, 24.dp),
                model = airQualityIcon,
                contentDescription = "空气质量"
//          contentScale = ContentScale.Fit,
            )
            Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
            Text(text = airQuality)
        }

    }
}

/**
 * 每时天气，每日天气
 */
@Composable
fun ListTitleItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}

@Composable

internal fun AlarmImageList(
    alarms: List<AlarmIcon>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (alarm in alarms) {
            AlarmImageItem(alarm = alarm)
        }
    }
    Spacer(modifier = modifier.height(24.dp))
}

@Composable
internal fun Temperature(
    weather: Weather,
    navigateToDistrictScreen: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = weather.temperature,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 68.sp),
                textAlign = TextAlign.Start,
            )

            Text(
                modifier = modifier.padding(vertical = 8.dp),
                text = weather.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
            )

            OutlinedButton(
                onClick = {
                    if (weather.stationName != "") {
                        navigateToDistrictScreen(
                            weather.cityId,
                            weather.stationId
                        )
                    }
                },
            ) {
                Text(
                    text = weather.stationName,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable

fun OneDayList(oneDays: List<OneDay>) {
    JianMoLazyRow {
        items(items = oneDays, key = { it.id }) {
            OneItem(
                topText = it.week,
                centerText = it.date,
                bottomText = it.t,
                dialogText = it.desc
            )
        }
    }
}

@Composable

fun OneHourList(oneHours: List<OneHour>) {
    JianMoLazyRow {
        items(items = oneHours, key = { it.id }) {
            OneItem(
                topText = it.hour,
                centerText = it.rain,
                bottomText = it.t
            )
        }
    }
}

@Composable

fun ConditionList(
    conditions: List<Condition>,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(16.dp))
    LazyRow {
        items(
            items = conditions,
            key = { it.name }
        ) {
            ConditionItem(condition = it)
        }
    }
}

@Composable

fun AlarmImageItem(
    alarm: AlarmIcon,
    modifier: Modifier = Modifier
) {
    var oneDayDescriptionPopupShown by remember { mutableStateOf(false) }
    if (oneDayDescriptionPopupShown) {
        DescriptionDialog(
            modifier = modifier.clickable { oneDayDescriptionPopupShown = false },
            description = alarm.name,
            onDismiss = { oneDayDescriptionPopupShown = false })
    }
    AsyncImage(// coil 异步下载网络图片
        modifier = modifier
            .size(44.dp, 40.dp)
            .padding(start = 2.dp)
            .clip(shape = RoundedCornerShape(percent = 10))
            .clickable(onClick = { oneDayDescriptionPopupShown = true }),
        model = alarm.iconUrl,
        contentDescription = null
    )
}

/**
 * 健康指数
 */
@Composable

fun ExponentItems(
    exponents: List<Exponent>,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        modifier = modifier
            .padding(vertical = 16.dp)
            .height(200.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(exponents) { exponent ->
            ExponentItem(
                title = exponent.title,
                levelDesc = exponent.levelDesc,
            )
        }
    }
}

@Composable

fun ExponentItem(
    title: String,
    levelDesc: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.width(300.dp)
        ) {
            // 如果有图标时插入此处
            Text(
                text = "$title · $levelDesc",
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                textAlign = TextAlign.Start
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }

        }
    }
}

@Composable

fun OneItem(
    modifier: Modifier = Modifier,
    topText: String,
    centerText: String,
    bottomText: String,
    dialogText: String = "",
) {
    var dialogShow by remember { mutableStateOf(false) }
    if (dialogShow) {
        DescriptionDialog(dialogText) { dialogShow = false }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(94.dp)
            .clickable(
                enabled = dialogText.isNotEmpty(),
                onClick = { dialogShow = true }
            )
    ) {
        Text(text = topText, fontWeight = FontWeight.Bold)
        Text(text = centerText, modifier.padding(vertical = 16.dp))
        Text(text = bottomText, fontWeight = FontWeight.Bold)
    }
}

@Composable

fun ConditionItem(
    condition: Condition,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(16.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(36.dp)
    ) {
        Column(
            modifier = modifier
                .width(148.dp)
                .height(90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = condition.name)
            Spacer(Modifier.height(8.dp))
            Text(text = condition.value, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun WeatherScreenTopBar(
    title: String,
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