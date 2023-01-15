package dev.shuanghua.ui.province

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.weather.data.android.model.Province

@Composable
fun ProvinceScreen(
    onBackClick: () -> Unit,
    navigateToCityScreen: (String, String) -> Unit,
    viewModel: ProvincesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProvinceScreen(
        refreshing = uiState.refreshing,
        provinces = uiState.provinces,
        updateProvince = { viewModel.refresh() },
        openCityScreen = navigateToCityScreen,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProvinceScreen(
    refreshing: Boolean,
    provinces: List<Province>,
    openCityScreen: (String, String) -> Unit,
    updateProvince: () -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val refreshState = rememberSwipeRefreshState(refreshing)

    Scaffold(
        topBar = {
            ProvinceScreenTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = refreshState,
            onRefresh = updateProvince,
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
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                items(
                    items = provinces,
                    key = { province -> province.id }
                ) { province ->
                    ProvinceItem(
                        province = province,
                        openCityListScreen = openCityScreen
                    )
                }
            }
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
fun ProvinceScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    TopAppBar(
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