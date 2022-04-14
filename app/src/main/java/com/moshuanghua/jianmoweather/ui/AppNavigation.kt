package com.moshuanghua.jianmoweather.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import jianmoweather.favorite.city.CityScreen
import jianmoweather.favorite.province.ProvinceScreen
import jianmoweather.home.favorite.FavoriteScreen
import jianmoweather.home.favorite.FavoritesScreen
import jianmoweather.home.more.MoreScreen
import jianmoweather.home.weather.WeatherScreen
import jianmoweather.module.common_ui_compose.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi

//sealed class TestScreen(private val route: String) {
//    //生成传入格式的 route : 先拿到 Screen的route ,然后拿 TestScreen的route
//    fun createRoute(root: Screen) = "${root.route}/$route" //目标接收路径，通常带有 {id} 包裹着
//
//    object Favorite : TestScreen("favorite")
//    object Province : TestScreen("province")
//    object City : TestScreen("city/{provinceId}") {        //说明当前 City 页面需要接收 provinceId
//        fun createValuesRoute(root: Screen, provinceId: String): String {
//            return "${root.route}/city/$provinceId"//生成包含传值内容的 route
//            // city/{provinceId/city/1234546}
//        }
//    }
//
//    object Weather : TestScreen("weather")
//
//    object More : TestScreen("more")
//}

@Composable
@ExperimentalAnimationApi
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = WeatherScreen.createRoute(), // weather
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() },
    ) {
        addWeatherNavGraph(navController)
        addFavoriteNavGraph(navController)
        addMoreNavGraph(navController)
    }
}

//---------------------------页面所属的模块----每个模块都需要有一个起始页面---------------------------------


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addWeatherNavGraph(navController: NavController) {
        addWeather(navController, Screen.Weather)
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFavoriteNavGraph(navController: NavController) {

    //FavoriteScreen   favorite
    composable(route = FavoriteScreen.createRoute()) {// 当前页面地址  favorite
        FavoritesScreen(
            openProvinceScreen = {
                navController.navigate(
                    route = "/favorite/addCity" //  别的页面地址(目标页面地址)
                )
            }
        )
    }

    navigation( // favorite 页面的某一个模块
        route = "/favorite/addCity",  //导航模块的 route 地址    favorite
        startDestination = ProvinceScreen.createRoute(Screen.Favorite)// 导航模块中的第一个默认显示页面地址
    ) {
        provinceAndCityList(navController, Screen.Favorite)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMoreNavGraph(navController: NavController) {
    addMore(navController, Screen.More)
}


//---------------------------每个具体页面--------------------------------------------------------------

// ( 同时定义每个页面的具体地址 这里不是用string 写死, 而是通过 TestScreen 类来拼接 生成每个页面的 string 地址)
@ExperimentalAnimationApi
private fun NavGraphBuilder.provinceAndCityList(navController: NavController, root: Screen) {
    //ProvinceScreen  /favorite/province
    composable(route = ProvinceScreen.createRoute(root)) {
        ProvinceScreen(
            openCityScreen = { provinceId ->
                navController.navigate(
                    route = CityScreen.createValueRoute(root, provinceId)
                    // favorite/city/12345678
                    // favorite/city/{provinceId}
                )
            }
        )
    }

    //CityScreen 页面需要 provinceId
    composable(route = CityScreen.createValueRoute(root)) { backStackEntry -> //favorite/city/{provinceId}
        val provinceId = backStackEntry.arguments?.getString("provinceId")
        requireNotNull(provinceId) { "ProvinceScreen -> CityScreen: provinceId wasn't found!" }

        CityScreen(
            provinceId = provinceId,
            openFavoriteScreen = {
                //cityId传到 ViewModel, FavoriteScreen在从 ViewModel中获取
                navController.popBackStack(
                    route = FavoriteScreen.createRoute(),// favorite
                    inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
                )
            })
    }
}


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addWeather(navController: NavController, root: Screen) {
    composable(route = WeatherScreen.createRoute()) { // weather/weather
        WeatherScreen()
    }
}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addMore(navController: NavController, root: Screen) {
    composable(route = MoreScreen.createRoute()) {
        MoreScreen()
    }
}


//-------------------------------------------------------------------------------------------------

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
