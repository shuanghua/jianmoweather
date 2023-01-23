package dev.shuanghua.ui.province

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.weather.data.android.model.Province

@Composable
fun ProvinceListScreen(
    onBackClick: () -> Unit,
    openCityListScreen: (String, String) -> Unit,
    viewModel: ProvincesViewModel = hiltViewModel(),
) {
    val uiState: ProvinceUiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProvinceListScreen(
        uiState = uiState,
        updateCityList = { viewModel.refresh() },
        openCityListScreen = openCityListScreen,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun ProvinceListScreen(
    uiState: ProvinceUiState,
    openCityListScreen: (String, String) -> Unit,
    updateCityList: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = updateCityList,
        refreshThreshold = 64.dp, //  拉动超过 60.dp 时,松开则触发自动转圈
        refreshingOffset = 56.dp  // 当松开，转圈的位置
    )

    Scaffold(
        topBar = {
            ProvinceListTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                items(
                    items = uiState.provinces,
                    key = { province -> province.id }
                ) { province ->
                    ProvinceItem(
                        province = province,
                        openCityListScreen = openCityListScreen
                    )
                }
            }

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


@Composable
fun ProvinceItem(
    province: Province,
    openCityListScreen: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    openCityListScreen(
                        province.id,
                        province.name
                    )
                }
            )
            .padding(8.dp)
    ) {
        Text(
            text = province.name,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 20.sp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = "省份列表") },
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

@Preview
@Composable
fun ProvinceItemPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "北京",
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 24.sp)
        )
    }
}