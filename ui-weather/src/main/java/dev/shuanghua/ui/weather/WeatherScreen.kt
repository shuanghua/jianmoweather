package dev.shuanghua.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.module.ui.compose.DescriptionDialog
import dev.shuanghua.module.ui.compose.rememberStateFlowWithLifecycle
import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.shared.extensions.ifNullToValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun WeatherScreen(openAirDetails: () -> Unit) {
    WeatherScreen(
        viewModel = hiltViewModel(),
        openAirDetails = openAirDetails
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@Composable
internal fun WeatherScreen(
    viewModel: WeatherViewModel,
    openAirDetails: () -> Unit,
    refresh: () -> Unit,
    onMessageShown: (id: Long) -> Unit
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by rememberStateFlowWithLifecycle(stateFlow = viewModel.uiStateFlow)
    uiState.message?.let { message ->
        scope.launch {
            snackBarHostState.showSnackbar(message.message)
            onMessageShown(message.id)
        }
    }

    Scaffold(
        topBar = {
            WeatherScreenTopBar(
                aqiText = uiState.temperature?.aqi.ifNullToValue(),
                stationText = uiState.temperature?.stationName.ifNullToValue(),
                title = uiState.temperature?.cityName.ifNullToValue(),
                scrollBehavior = scrollBehavior,
                openAirDetails = openAirDetails
            )
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.refreshing),
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
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                ),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                if (uiState.alarms.isNotEmpty()) item { AlarmImageList(uiState.alarms) }
                uiState.temperature?.let { item { TemperatureText(temperature = it) } }
                if (uiState.oneHours.isNotEmpty()) item { OneHourList(oneHours = uiState.oneHours) }
                if (uiState.oneDays.isNotEmpty()) item { OneDayList(oneDays = uiState.oneDays) }
                if (uiState.others.isNotEmpty()) item { ConditionList(conditions = uiState.others) }
                if (uiState.exponents.isNotEmpty()) item { ExponentItems(exponents = uiState.exponents) }
            }
        }
        if (uiState.refreshing){
            LinearProgressIndicator(modifier = Modifier.padding(innerPadding).fillMaxWidth())
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    openAirDetails: () -> Unit,
) {
    WeatherScreen(
        viewModel = viewModel,
        openAirDetails = openAirDetails,
        refresh = { viewModel.refresh() },
        onMessageShown = { viewModel.clearMessage(it) }
    )
}

@Composable
internal fun AlarmImageList(alarms: List<Alarm>) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        for (alarm in alarms) {
            AlarmImageItem(alarm = alarm)
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
internal fun TemperatureText(
    temperature: Temperature
) {
    Surface(
        modifier = Modifier.padding(38.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(36.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = temperature.temperature,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 68.sp),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 26.dp, top = 42.dp)
            )

            Text(
                text = temperature.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun OneDayList(
    modifier: Modifier = Modifier,
    oneDays: List<OneDay>
) {
    LazyRow(
        modifier = modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = oneDays,
            key = { it.id }
        ) {
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
    modifier: Modifier = Modifier,
    oneHours: List<OneHour>
) {
    LazyRow(
        modifier = modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = oneHours,
            key = { it.id }
        ) {
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
    conditions: List<Condition>
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
    var oneDayDescriptionPopupShown by remember { mutableStateOf(false) }//state的更改会重新执行当前函数

    if (oneDayDescriptionPopupShown) {//
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
            .clickable(onClick = {
                oneDayDescriptionPopupShown = true
            }),
        model = alarm.icon,
//        contentScale = ContentScale.Fit,
        contentDescription = null
    )
}

/**
 * 健康指数
 */
@Composable
fun ExponentItems(
    modifier: Modifier = Modifier,
    exponents: List<Exponent>
) {
    Surface(
        modifier = modifier.padding(16.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(40.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "健康指数",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )

            val mod = Modifier
                .width(150.dp)
                .padding(top = 16.dp)

            FlowRow(
                mainAxisAlignment = MainAxisAlignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (exponents.size % 2 != 0) {
                    for (i in exponents.indices - 1) {
                        ExponentItem(
                            exponents[i].title,
                            exponents[i].levelDesc,
                            modifier = mod
                        )
                    }
                } else {
                    for (i in exponents.indices) {
                        ExponentItem(
                            exponents[i].title,
                            exponents[i].levelDesc,
                            modifier = mod
                        )
                    }
                }
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
    dialogText: String = ""
) {
    var dialogShow by remember { mutableStateOf(false) }
    if (dialogShow) {
        DescriptionDialog(description = dialogText) {
            dialogShow = false
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp)
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
    modifier: Modifier = Modifier
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

@Composable
fun ExponentItem(
    title: String,
    levelDesc: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = levelDesc)
    }
}

/**
 * 标题
 */
@Composable
fun WeatherScreenTopBar(
    modifier: Modifier = Modifier,
    title: String,
    aqiText: String,
    stationText: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    openAirDetails: () -> Unit
) {
    // 从上层到下层: status图标，Status背景色, TopBar ,  Content
    val foregroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    )
    val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior?.scrollFraction ?: 0f  //  离开顶部时设置为 surfaceColor, 否则使用默认
    ).value

    Surface(modifier = modifier, color = backgroundColor) {
        CenterAlignedTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
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
                Text(
                    text = stationText,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

