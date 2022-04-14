package com.moshuanghua.jianmoweather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import jianmoweather.home.weather.WeatherScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalAnimationApi::class)
fun NavGraphBuilder.weatherNavGraph(navController: NavController) {
    composable(route = WeatherScreen.route()) { // weather/weather
        WeatherScreen(openAirDetails = {

        })
    }
}