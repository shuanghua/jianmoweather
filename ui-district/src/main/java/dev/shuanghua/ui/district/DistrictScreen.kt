package dev.shuanghua.ui.district

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.shuanghua.core.ui.theme.topBarBackgroundColor
import dev.shuanghua.core.ui.theme.topBarForegroundColors
import dev.shuanghua.module.ui.compose.rememberStateFlowWithLifecycle
import dev.shuanghua.weather.data.db.entity.District

@Composable
fun DistrictScreen(
    onBackClick: () -> Unit,
    navigateToStationScreen: (String) -> Unit,
    viewModel: DistrictViewModel = hiltViewModel(),
) {
    val state by rememberStateFlowWithLifecycle(viewModel.uiState)

    DistrictScreen(
        list = state.list,// uiState.districtList
        refreshing = state.loading,// uiState.loading
        refresh = { viewModel.refresh() },
        onBackClick = onBackClick,
        openStationScreen = navigateToStationScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DistrictScreen(
    list: List<District>,
    refreshing: Boolean,
    refresh: () -> Unit,
    openStationScreen: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }
    val refreshState = rememberSwipeRefreshState(refreshing)

    Scaffold(
        topBar = {
            DistrictTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = refreshState,
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
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                items(
                    items = list,
                    key = { district -> district.name }
                ) { district ->
                    DistrictItem(
                        district = district,
                        openStationScreen = openStationScreen
                    )
                }
            }
        }
    }
}

@Composable
fun DistrictItem(
    district: District,
    openStationScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { openStationScreen(district.name) }
            )
            .padding(8.dp)
    ) {
        Text(
            text = district.name,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 20.sp)
        )
    }
}

@Composable
fun DistrictTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier,
        color = topBarBackgroundColor(scrollBehavior = scrollBehavior!!)
    ) {
        SmallTopAppBar(
            modifier = modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            colors = topBarForegroundColors(),
            title = { Text(text = "区县") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        )
    }
}