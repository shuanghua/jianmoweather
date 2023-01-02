package dev.shuanghua.ui.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.shuanghua.module.ui.compose.components.*
import dev.shuanghua.weather.data.network.ShenZhenWeatherApi

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    navigateToProvinceScreen: () -> Unit = {},
) {
    val stationUiState by viewModel.stationUiState.collectAsStateWithLifecycle()
    val cityUiState by viewModel.cityUiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        stationUiState = stationUiState,
        cityUiState = cityUiState,
        deleteStation = { viewModel.deleteStation(it) },
        deleteCity = { viewModel.deleteCity(it) },
        openProvinceScreen = navigateToProvinceScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    stationUiState: FavoriteStationUiState,
    cityUiState: FavoriteCityUiState,
    deleteStation: (String) -> Unit,
    deleteCity: (String) -> Unit,
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
        FavoriteList(
            stationUiState = stationUiState,
            cityUiState = cityUiState,
            scrollBehavior = scrollBehavior,
            deleteStation = deleteStation,
            deleteCity = deleteCity,
            innerPadding = innerPadding,
        )
    }
}


private val defaultRoundedCornerSize = 26.dp
private val defaultHorizontalSize = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteList(
    stationUiState: FavoriteStationUiState,
    cityUiState: FavoriteCityUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    deleteStation: (String) -> Unit,
    deleteCity: (String) -> Unit,
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
            .padding(horizontal = defaultHorizontalSize)
    ) {
        when (stationUiState) {
            FavoriteStationUiState.Loading -> {}
            is FavoriteStationUiState.Success -> {
                item { Text(text = "站点") }
                stationUiState.stationWeather.forEach {
                    item(key = it.stationName) {
                        FavoriteStationItem(station = it)
                    }
                }
            }
        }

        when (cityUiState) {
            FavoriteCityUiState.Loading -> {}
            is FavoriteCityUiState.Success -> {
                item { Text(text = "城市") }
                cityUiState.cityWeather.forEach {
                    item(key = it.cityId) {
                        FavoriteCityItem(cityWeather = it)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteStationItem(
    station: StationWeather,
    modifier: Modifier = Modifier,
) {

    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .height(120.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
    ) {
        Text(
            modifier = modifier.padding(16.dp),
            text = station.stationName,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun FavoriteCityItem(
    cityWeather: CityWeather,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier
            .height(120.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(defaultRoundedCornerSize))
    ) {
        Row {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = defaultHorizontalSize),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = modifier.padding(top = 8.dp),
                    text = cityWeather.cityName,
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
                    text = cityWeather.temperature,
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
                    model = ShenZhenWeatherApi.ICON_HOST + cityWeather.iconUrl,
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
    scrollBehavior: TopAppBarScrollBehavior,
    openProvinceListScreen: () -> Unit,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
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