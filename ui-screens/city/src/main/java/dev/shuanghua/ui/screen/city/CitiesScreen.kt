package dev.shuanghua.ui.screen.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import org.koin.androidx.compose.koinViewModel

/**
 * 选择城市后，将城市存到数据库的收藏表
 */
@Composable
fun CitiesScreen(
	navigateToFavoriteScreen: () -> Unit,
	onBackClick: () -> Unit,
	viewModel: CitiesViewModel = koinViewModel(),
) {
	val uiState: CityUiState by viewModel.uiState.collectAsStateWithLifecycle()

	CitiesScreen(
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
internal fun CitiesScreen(
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
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "返回"
				)
			}
		}
	)
}