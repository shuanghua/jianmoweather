package com.moshuanghua.jianmoweather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import jianmoweather.home.favorite.FavoriteScreen
import jianmoweather.home.more.MoreScreen
import jianmoweather.home.weather.WeatherScreen


@Composable
fun rememberJianMoAppState(
    navController: NavHostController
) = remember(navController) { JianMoAppState(navController) }

@Immutable
class JianMoAppState(private val navController: NavController) {
    // 当显示以下三个页面时,让 BottomBar 显示
    private val bottomBarRoutes = listOf("weather", "favorite", "more")
    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route in bottomBarRoutes
}