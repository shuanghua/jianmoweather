package com.moshuanghua.jianmoweather.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
        listOf(TestScreen.Favorite.createFormatRoute(Screen.Favorite), Screen.Weather.route, Screen.More.route)
    val shouldShowBottomBar: Boolean
        @Composable get()
        = navController.currentBackStackEntryAsState()
            .value?.destination?.route in bottomBarRoutes
}