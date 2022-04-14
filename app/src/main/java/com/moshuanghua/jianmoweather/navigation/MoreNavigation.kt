package com.moshuanghua.jianmoweather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import jianmoweather.home.more.MoreScreen

@ExperimentalAnimationApi
fun NavGraphBuilder.moreNavGraph(navController: NavController) {
    composable(route = MoreScreen.route()) {
        MoreScreen()
    }
}