package dev.shuanghua.ui.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.shuanghua.core.ui.theme.topBarBackgroundColor
import dev.shuanghua.core.ui.theme.topBarForegroundColors
import dev.shuanghua.weather.data.db.entity.City

/**
 * 选择城市后，将城市存到数据库的收藏表
 */
@Composable
fun CityScreen(
    navigateToFavoriteScreen: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CityViewModel = hiltViewModel()
) {
//    Box(modifier = Modifier.statusBarsPadding()) {
//        Button(onClick = { openFavoriteScreen() }) {
//            Text(text = "$provinceId $provinceName 的城市列表")
//        }
//    }

//    viewModel.getCityList(provinceId)
    CityScreen(
        onBackClick = onBackClick,
        addCityIdToFavorite = { cityId ->
            viewModel.addCityIdToFavorite(cityId) //添加成功后，在viewModel调用页面跳转
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CityScreen(
    topBarTitle: String = "选择城市",
    cityList: List<City> = emptyList(),
    addCityIdToFavorite: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val topAppBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(topAppBarScrollState) }

    Scaffold(
        topBar = {
            CityScreenTopBar(
                provinceName = topBarTitle,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            items(
                items = cityList,
                key = { city -> city.id }
            ) { city ->
                CityItem(
                    city = city,
                    addCityIdToFavorite = addCityIdToFavorite
                )
            }
        }
    }
}


@Composable
fun CityItem(
    city: City,
    addCityIdToFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                addCityIdToFavorite(city.id)
            })
            .padding(8.dp)
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 20.sp)
        )
    }
}


@Composable
fun CityScreenTopBar(
    provinceName: String,
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
}