package jianmoweather.home.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.module.common_ui_compose.rememberStateFlowWithLifecycle

@Composable
fun FavoritesScreen(openProvinceScreen: () -> Unit = {}) {
    FavoritesScreen(
        viewModel = hiltViewModel(),
        openProvinceScreen = openProvinceScreen
    )
}

@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel,
    openProvinceScreen: () -> Unit
) {
    FavoritesScreen(
        viewModel = viewModel,
        refreshAction = {},
        openProvinceScreen = openProvinceScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    viewModel: FavoriteViewModel,
    refreshAction: () -> Unit,
    openProvinceScreen: () -> Unit
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val loadingState by rememberStateFlowWithLifecycle(viewModel.loadingStateFlow)

    Scaffold(
        topBar = {
            FavoriteScreenTopBar(
                scrollBehavior = scrollBehavior,
                openProvinceListScreen = openProvinceScreen
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = loadingState.isLoading),
            onRefresh = refreshAction,
            refreshTriggerDistance = 60.dp,
            indicatorPadding = paddingValues,
            indicator = { _state, _trigger ->
                SwipeRefreshIndicator(
                    state = _state,
                    refreshTriggerDistance = _trigger,
                    scale = true
                )
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 60.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
                    .padding(paddingValues),

                ) {
                repeat(30) {
                    item { FavoriteCityWeatherItem() }
                }
            }
        }
    }
}

@Composable
fun FavoriteScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    openProvinceListScreen: () -> Unit
) {
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
        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            title = { Text(text = "收藏城市") },
            actions = {
                IconButton(onClick = openProvinceListScreen) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
                // 添加更多按钮
            }
        )
    }
}

@Preview
@Composable
fun FavoriteCityWeatherItem(
    modifier: Modifier = Modifier,
    t: String = "30°C",
    desc: String = "阵雨",
    cityName: String = "河池.罗城"
) {
    Surface(
        modifier = modifier.padding(16.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = {}), // TODO: 查看收藏城市天气
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = cityName,
                    modifier = modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = desc,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(vertical = 16.dp)
                )
            }
            Text(
                text = t,
                modifier = modifier.padding(end = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewFavorite() {
//    Box(modifier = Modifier.statusBarsPadding()) {
//        AnimatedCircle(
//            modifier = Modifier
//                .height(300.dp)
//                .align(Alignment.Center)
//                .fillMaxWidth()
//        )
//    }

    FavoritesScreen()
}