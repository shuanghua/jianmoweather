package dev.shuanghua.ui.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.module.ui.compose.rememberStateFlowWithLifecycle
import dev.shuanghua.module.ui.compose.widget.*
import dev.shuanghua.weather.data.db.entity.Favorite
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

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
        refreshAction = { viewModel.refresh() },
        openProvinceScreen = openProvinceScreen,
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    viewModel: FavoriteViewModel,
    refreshAction: () -> Unit,
    openProvinceScreen: () -> Unit,
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    val uiState by rememberStateFlowWithLifecycle(viewModel.uiState)

    val channel = remember { Channel<Int> { Channel.CONFLATED } }

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(channel) {
        channel.receiveAsFlow().collect { index ->
            val oldList = uiState.favorites //在删除之前,先临时保存旧集合,以便当用户撤回时还原
            val removeItem = oldList[index]
            viewModel.deleteFavorite(removeItem)
            val result = snackBarHostState.showSnackbar(
                message = "已删除一个城市",
                actionLabel = "撤销"
            )
            when (result) {
                // 最优情况应该是:
                // 先移除ui的集合数据
                // 如果用户撤回,则还原ui集合
                // 否则再进一步从数据库中移除
                // 由于这里使用绝对的单一数据源(数据库) ,所以始终保持对数据库的数据来操作
                SnackbarResult.ActionPerformed -> viewModel.addAllFavorite(oldList)
                SnackbarResult.Dismissed -> {}
            }
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
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.refreshing),
            onRefresh = refreshAction,
            indicatorPadding = innerPadding,
            indicator = { _state, _trigger ->
                SwipeRefreshIndicator(
                    state = _state,
                    refreshTriggerDistance = _trigger,
                    scale = true
                )
            }
        ) {
            FavoriteList(
                favorites = uiState.favorites,
                scrollBehavior = scrollBehavior,
                innerPadding = innerPadding,
                removeFavoriteItem = { index -> channel.trySend(index) }
            )
        }
        if (uiState.refreshing){
            LinearProgressIndicator(modifier = Modifier.padding(innerPadding).fillMaxWidth())
        }
    }
}

private val defaultRoundedCornerSize = 26.dp
private val defaultHorizontalSize = 16.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteList(
    favorites: List<Favorite>,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    removeFavoriteItem: (Int) -> Unit
) {

    LazyColumn(
        contentPadding = innerPadding,
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(horizontal = defaultHorizontalSize)
    ) {
        itemsIndexed(
            items = favorites,
            key = { _, item -> item.cityName }
        ) { index, favorite ->

            val dismissState: DismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    return@rememberDismissState when (dismissValue) {
                        DismissValue.DismissedToStart,
                        DismissValue.DismissedToEnd -> {
                            removeFavoriteItem(index)
                            true
                        }
                        else -> false
                    }
                }
            )
            SwipeToDismiss(
                state = dismissState,
                background = { UnderFavoriteItem(dismissState.dismissDirection) },
                dismissContent = { FavoriteCityWeatherItem(favorite = favorite) },
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(100.dp)
                    .animateItemPlacement()
                    .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
                    .clickable {

                    },
            )
        }
    }
}

@Composable
fun UnderFavoriteItem(direction: DismissDirection?) {
    direction ?: return // 没有捕获到拖动
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }

    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
            .background(Color(0xFFFFB4A9))
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = Color(0xFF410001),
            contentDescription = "delete",
            modifier = Modifier.padding(horizontal = 32.dp)

        )
    }
}

@Composable
fun FavoriteCityWeatherItem(
    modifier: Modifier = Modifier,
    favorite: Favorite,
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = defaultHorizontalSize)
            ) {
                Text(
                    text = favorite.cityName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = favorite.cityid,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = favorite.maxT,
                modifier = modifier.padding(end = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun FavoriteScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    openProvinceListScreen: () -> Unit,
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