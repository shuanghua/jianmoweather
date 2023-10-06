package dev.shuanghua.ui.screen.station

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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


@Composable
fun StationListRoute(
    onBackClick: () -> Unit,
    openWeatherScreen: () -> Unit,
    viewModel: StationViewModel = hiltViewModel(),
) {
    val uiState: StationsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    StationListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        openWeatherScreen = { obtId ->
            viewModel.saveSelectedStation(obtId)
            openWeatherScreen()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationListScreen(
    uiState: StationsUiState,
    onBackClick: () -> Unit,
    openWeatherScreen: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            StationTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            when (uiState) {
                is StationsUiState.NoData -> {}
                is StationsUiState.HasData -> {
                    items(
                        items = uiState.stationList,
                        key = { station -> station.stationName }
                    ) { station ->
                        JmoTextItem(
                            text = station.stationName,
                            onClick = { openWeatherScreen(station.stationId) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = "站点") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回"
                )
            }
        }
    )
}