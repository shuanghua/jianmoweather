package dev.shuanghua.ui.favorite

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.core.ui.topBarForegroundColors
import dev.shuanghua.module.ui.compose.components.*
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.data.network.ShenZhenService
import timber.log.Timber

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    navigateToProvinceScreen: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        uiState = uiState,
        list = uiState.favorites,
        refreshAction = { viewModel.refresh() },
        deleteDbFavorite = { viewModel.deleteFavorite(it) },
        openProvinceScreen = navigateToProvinceScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    uiState: FavoriteUiState,
    list: List<FavoriteCityWeather>,
    refreshAction: () -> Unit,
    deleteDbFavorite: (String) -> Unit,
    openProvinceScreen: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }

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
            state = rememberSwipeRefreshState(isRefreshing = uiState.loading),
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
            Timber.d("---------$list---->>")

            FavoriteList(
                favorites = list,
                scrollBehavior = scrollBehavior,
                deleteFavorite = deleteDbFavorite,
                innerPadding = innerPadding,
            )
        }
    }
}

private val defaultRoundedCornerSize = 26.dp
private val defaultHorizontalSize = 16.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteList(
    favorites: List<FavoriteCityWeather>,
    scrollBehavior: TopAppBarScrollBehavior,
    deleteFavorite: (String) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(horizontal = defaultHorizontalSize)
    ) {
        itemsIndexed(
            items = favorites,
            key = { _, item -> item.cityName }
        ) { _, favorite ->
            var deleted by remember { mutableStateOf(false) }
            val dismissState: DismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    return@rememberDismissState when (dismissValue) {
                        DismissValue.DismissedToStart,
                        DismissValue.DismissedToEnd,
                        -> {
                            deleted = true
                            true
                        }

                        else -> false
                    }
                }
            )

            if (deleted) {
                deleteFavorite(favorite.cityid)
            }

            SwipeToDismiss(
                state = dismissState,
                background = { UnderFavoriteItem(dismissState) },
                dismissContent = {
                    FavoriteItem(
                        cityName = favorite.cityName,
                        temperature = favorite.maxT,
                        iconPath = favorite.wtype
                    )
                },
                directions = setOf(
                    DismissDirection.StartToEnd,
                    DismissDirection.EndToStart
                ),
                modifier = Modifier
                    .height(110.dp)
                    .animateItemPlacement()
                    .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
                    .clickable {},
            )
        }
    }
}

@Composable
fun UnderFavoriteItem(direction: DismissState) {
    direction.dismissDirection ?: return // 没有捕获到拖动
    val alignment = when (direction.dismissDirection) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        else -> Alignment.CenterEnd
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
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun FavoriteItem(
    cityName: String,
    temperature: String,
    iconPath: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
    ) {
//        Column(modifier = modifier.padding(16.dp)) {
//            Row {
//                Text(
//                    modifier = modifier.weight(1f),
//                    text = favorite.cityName,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//                Text(
//                    text = favorite.maxT,
//                    modifier = modifier.padding(end = 8.dp, bottom = 8.dp),
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            }
//
//            Row(horizontalArrangement = Arrangement.Center) {
//                Text(
//                    modifier = modifier.weight(1f),
//                    text = favorite.cityid,
//                    fontWeight = FontWeight.Bold,
//                )
//
//                AsyncImage(// coil 异步下载网络图片
//                    modifier = modifier
//                        .size(100.dp, 50.dp)
//                        .padding(start = 2.dp)
//                        .clip(shape = RoundedCornerShape(percent = 10)),
//                    model = ShenZhenService.ICON_HOST + favorite.wtype,
//                    contentScale = ContentScale.Fit,
//                    contentDescription = null
//                )
//            }
//        }


        Row {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = defaultHorizontalSize),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = modifier.padding(top = 8.dp),
                    text = cityName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = temperature,
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
                    model = ShenZhenService.ICON_HOST + iconPath,
//                  contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    openProvinceListScreen: () -> Unit,
) {
    TopAppBar(
        modifier = modifier.statusBarsPadding(),
        scrollBehavior = scrollBehavior,
        colors = topBarForegroundColors(),
        title = { Text(text = "收藏城市") },
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