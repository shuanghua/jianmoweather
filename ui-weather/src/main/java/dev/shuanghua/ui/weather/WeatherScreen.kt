package dev.shuanghua.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.module.ui.compose.DescriptionDialog
import dev.shuanghua.module.ui.compose.JianMoLazyRow
import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.shared.extensions.ifNullToValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalLifecycleComposeApi::class)
@ExperimentalCoroutinesApi
@Composable
fun WeatherScreen(
    openAirDetails: () -> Unit,
    navigateToDistrictScreen: (String, String) -> Unit,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    WeatherScreen(
        uiState = uiState,
        openAirDetails = openAirDetails,
        refresh = { viewModel.refresh() },
        navigateToDistrictScreen = navigateToDistrictScreen,
        addToFavorite = { viewModel.addToFavorite() },
        onMessageShown = { viewModel.clearMessage(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    openAirDetails: () -> Unit,
    refresh: () -> Unit,
    navigateToDistrictScreen: (String, String) -> Unit,
    addToFavorite: () -> Unit,
    onMessageShown: (id: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    uiState.message?.let { message ->
        scope.launch {
            snackBarHostState.showSnackbar(message.message)
            onMessageShown(message.id)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            WeatherScreenTopBar(
                aqiText = uiState.temperature?.aqi.ifNullToValue(),
                title = uiState.temperature?.cityName.ifNullToValue(),
                scrollBehavior = scrollBehavior,
                openAirDetails = openAirDetails,
                addToFavorite = addToFavorite
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.loading),
            onRefresh = refresh,
            indicatorPadding = innerPadding,
            indicator = { _state, _trigger ->
                SwipeRefreshIndicator(
                    state = _state,
                    refreshTriggerDistance = _trigger,
                    scale = true
                )
            }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                if (!uiState.alarms.isNullOrEmpty()) item {
                    AlarmImageList(uiState.alarms)
                }


                uiState.temperature?.let {
                    item {
                        Temperature(
                            temperature = it,
                            navigateToDistrictScreen = navigateToDistrictScreen,
                        )
                    }
                }


                if (!uiState.oneHours.isNullOrEmpty()) {
                    item { ListTitleItem("每时天气") }
                    item { OneHourList(oneHours = uiState.oneHours) }
                }


                if (!uiState.oneDays.isNullOrEmpty()) {
                    item { ListTitleItem("每日天气") }
                    item { OneDayList(oneDays = uiState.oneDays) }
                }


                if (!uiState.others.isNullOrEmpty()) item {
                    ConditionList(conditions = uiState.others)
                }


                if (!uiState.exponents.isNullOrEmpty()) item {
                    ExponentItems(exponents = uiState.exponents)
                }
            }
        }
        if (uiState.loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
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
internal fun AlarmImageList(alarms: List<Alarm>) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (alarm in alarms) {
            AlarmImageItem(alarm = alarm)
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
internal fun Temperature(
    temperature: Temperature,
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
                text = temperature.temperature,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 68.sp),
                textAlign = TextAlign.Start,
            )

            Text(
                modifier = modifier.padding(vertical = 8.dp),
                text = temperature.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
            )

            OutlinedButton(
                onClick = {
                    navigateToDistrictScreen(
                        temperature.cityId,
                        temperature.obtId.ifEmpty { temperature.autoStationId }
                    )
                },
            ) {
                Text(
                    text = temperature.stationName,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun OneDayList(
    oneDays: List<OneDay>,
    modifier: Modifier = Modifier,
) {
    JianMoLazyRow(modifier = modifier) {
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
fun OneHourList(
    oneHours: List<OneHour>,
    modifier: Modifier = Modifier,
) {
    JianMoLazyRow(modifier = modifier) {
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
    modifier: Modifier = Modifier,
    conditions: List<Condition>,
) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(modifier) {
        items(
            items = conditions,
            key = { it.name }
        ) {
            ConditionItem(condition = it)
        }
    }
}

@Composable
fun AlarmImageItem(modifier: Modifier = Modifier, alarm: Alarm) {
    var oneDayDescriptionPopupShown by remember { mutableStateOf(false) }
    if (oneDayDescriptionPopupShown) {
        DescriptionDialog(
            modifier = Modifier.clickable { oneDayDescriptionPopupShown = false },
            description = alarm.name,
            onDismiss = { oneDayDescriptionPopupShown = false })
    }
    AsyncImage(// coil 异步下载网络图片
        modifier = modifier
            .size(44.dp, 40.dp)
            .padding(start = 2.dp)
            .clip(shape = RoundedCornerShape(percent = 10))
            .clickable(onClick = { oneDayDescriptionPopupShown = true }),
        model = alarm.icon,
        contentDescription = null
    )
}

/**
 * 健康指数
 */
@Composable
fun ExponentItems(
    exponents: List<Exponent>,
    modifier: Modifier = Modifier,
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
            modifier = Modifier
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
    modifier: Modifier = Modifier,
    title: String,
    aqiText: String,
    scrollBehavior: TopAppBarScrollBehavior,
    openAirDetails: () -> Unit,
    addToFavorite: () -> Unit,
) {
    // 从上层到下层: status图标，Status背景色, TopBar ,  Content

    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            Text(
                text = aqiText,
                modifier = modifier
                    .clickable(onClick = openAirDetails)
                    .clip(shape = CircleShape)
                    .padding(16.dp)
            )
        },
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

