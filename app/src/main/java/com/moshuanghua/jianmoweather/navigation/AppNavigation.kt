package com.moshuanghua.jianmoweather.navigation

import androidx.compose.animation.*
import androidx.navigation.*
import androidx.navigation.compose.composable
import jianmoweather.favorite.city.CityListScreen
import jianmoweather.favorite.province.ProvinceListScreen
import jianmoweather.home.favorite.FavoritesScreen
import jianmoweather.home.more.MoreScreen
import jianmoweather.home.weather.WeatherScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * APP 下所有页面的 route
 */
sealed class Screen(val route: String) {
    object Favorite : Screen("screen_favorite")

    /** [FavoritesScreen]   */
    object Weather : Screen("screen_weather")

    /** [WeatherScreen]     */
    object More : Screen("screen_more")

    /** [MoreScreen]        */

    object Province : Screen("screen_province")

    /** [ProvinceListScreen] */
    object CityList : Screen("screen_city/{provinceId}") {
        /** [CityListScreen] */
        /**
         * 用来创建传值的 route
         */
        fun argsRoute(provinceId: String) = "screen_city/$provinceId"
    }
}

sealed class Module(val route: String) {
    object AddCity : Module("module_addcity")
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.jianMoWeatherNavigation(navController: NavController) {
    composable(route = Screen.Weather.route) { WeatherScreen(openAirDetails = {}) }
    addFavoriteNavGraph(navController)
    composable(route = Screen.More.route) { MoreScreen() }
}

/**
 * FavoriteScreen 内的导航
 */
@ExperimentalAnimationApi
fun NavGraphBuilder.addFavoriteNavGraph(navController: NavController) {

    /** [FavoritesScreen] */
    composable(route = Screen.Favorite.route) {// 当前页面地址  favorite
        FavoritesScreen {
            navController.navigate(route = Module.AddCity.route) //  别的页面地址(目标页面地址)
        }
    }

    navigation(
        route = Module.AddCity.route, // 当前模块路由地址
        startDestination = Screen.Province.route //当前模块默认显示页面地址
    ) {

        /** [ProvinceListScreen] */
        composable(route = Screen.Province.route) {
            ProvinceListScreen(openCityScreen = { provinceId ->
                navController.navigate(
                    route = Screen.CityList.argsRoute(provinceId)
                )
            })
        }

        /** [CityListScreen] */
        composable(route = Screen.CityList.route) { backStackEntry ->
            val provinceId = backStackEntry.arguments?.getString("provinceId")
            requireNotNull(provinceId) { "ProvinceScreen -> CityScreen: provinceId wasn't found!" }
            CityListScreen(provinceId = provinceId, openFavoriteScreen = {
                navController.popBackStack(  // cityId 传到 ViewModel, FavoriteScreen 在从 ViewModel 中获取
                    route = Screen.Favorite.route, // favorite
                    inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
                )
            })
        }
    }
}
