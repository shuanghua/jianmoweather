package dev.shuanghua.ui.screen.city

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.components.JmoTextItem

/**
 * 选择城市后，将城市存到数据库的收藏表
 */
@Composable
fun CityListRoute(
    navigateToFavoriteScreen: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CityListViewModel = hiltViewModel(),
) {
    val uiState: CityUiState by viewModel.uiState.collectAsStateWithLifecycle()

    CityListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        addCityIdToFavorite = { cityId ->
            viewModel.addCityIdToFavorite(cityId) //添加成功后，在viewModel调用页面跳转
            navigateToFavoriteScreen()
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CityListScreen(
    uiState: CityUiState,
    addCityIdToFavorite: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when (uiState) {
        is CityUiState.NoData -> {}
        is CityUiState.HasData -> {
            Scaffold(
                topBar = {
                    CityScreenTopBar(
                        provinceName = uiState.provinceName,
                        scrollBehavior = topAppBarScrollBehavior,
                        onBackClick = onBackClick
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = innerPadding.calculateBottomPadding() + 16.dp,
                        top = innerPadding.calculateTopPadding(),
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                        .fillMaxSize()
                ) {
                    items(
                        items = uiState.cityList,
                        key = { city -> city.id }
                    ) { city ->
                        JmoTextItem(
                            text = city.name,
                            onClick = { addCityIdToFavorite(city.id) }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreenTopBar(
    provinceName: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = provinceName) },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "返回"
                )
            }
        }
    )
}