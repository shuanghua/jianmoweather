package dev.shuanghua.ui.screen.station

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shuanghua.ui.core.components.JmoTextItem
import dev.shuanghua.weather.data.android.model.Station
import org.koin.androidx.compose.koinViewModel


@Composable
fun StationListRoute(
	onBackClick: () -> Unit,
	openWeatherScreen: () -> Unit,
	viewModel: StationViewModel = koinViewModel(),
) {
	val uiState: StationsUiState by viewModel.uiState.collectAsStateWithLifecycle()

	StationListScreen(
		uiState = uiState,
		onBackClick = onBackClick,
		openWeatherScreen = {
			viewModel.saveSelectedStation(it)
			openWeatherScreen()
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationListScreen(
	uiState: StationsUiState,
	onBackClick: () -> Unit,
	openWeatherScreen: (Station) -> Unit,
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
					) { station: Station ->
						JmoTextItem(
							text = station.stationName,
							onClick = {
								openWeatherScreen(station)
							}
						)
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