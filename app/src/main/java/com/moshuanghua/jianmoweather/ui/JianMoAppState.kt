package com.moshuanghua.jianmoweather.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import jianmoweather.home.favorite.FavoriteScreen
import jianmoweather.home.more.MoreScreen
import jianmoweather.home.weather.WeatherScreen
import jianmoweather.module.common_ui_compose.Screen

@Composable
fun rememberJianMoAppState(
    navController: NavHostController
) = remember(navController) {
    JianMoAppState(navController)
}


class JianMoAppState(
    private val navController: NavController
) {
    private val bottomBarRoutes =
        listOf(
            WeatherScreen.createRoute(),
            FavoriteScreen.createRoute(),
            MoreScreen.createRoute(),
        )
    val shouldShowBottomBar: Boolean
        @Composable get()
        = navController.currentBackStackEntryAsState()
            .value?.destination?.route in bottomBarRoutes
}