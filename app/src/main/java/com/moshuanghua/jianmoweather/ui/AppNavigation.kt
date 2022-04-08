package com.moshuanghua.jianmoweather.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import jianmoweather.favorite.city.CityScreen
import jianmoweather.favorite.province.ProvinceScreen
import jianmoweather.home.favorite.FavoritesScreen
import jianmoweather.home.more.MoreScreen
import jianmoweather.home.weather.WeatherScreen

sealed class Screen(val route: String) {
    object Favorite : Screen("favorite")
    object Weather : Screen("weather")
    object More : Screen("more")

}

sealed class TestScreen(private val route: String) {
    //生成传入格式的 route
    fun createFormatRoute(root: Screen) = "${root.route}/$route" //目标接收路径，通常带有 {id} 包裹着

    object Favorite : TestScreen("favorite")
    object Province : TestScreen("province")
    object City : TestScreen("city/{provinceId}") {        //说明当前 City 页面有接收 provinceId 的需求
        fun createValuesRoute(root: Screen, provinceId: String): String {
            return "${root.route}/city/$provinceId"//生成包含传值内容的 route
        }
    }

    object Weather : TestScreen("weather")

    object More : TestScreen("more")
}

@Composable
@ExperimentalAnimationApi
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Weather.route,
        enterTransition = { defaultTiviEnterTransition(initialState, targetState) },
        exitTransition = { defaultTiviExitTransition(initialState, targetState) },
        popEnterTransition = { defaultTiviPopEnterTransition() },
        popExitTransition = { defaultTiviPopExitTransition() },
    ) {
        addFavoriteNavGraph(navController)
        addWeatherNavGraph(navController)
        addMoreNavGraph(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFavoriteNavGraph(navController: NavController) {
    navigation(//模块导航
        route = Screen.Favorite.route,//导航模块的 route 地址
        startDestination = TestScreen.Favorite.createFormatRoute(Screen.Favorite)// 导航模块中的第一个默认显示页面地址
    ) {
        addFavorite(navController, Screen.Favorite)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFavorite(navController: NavController, root: Screen) {

    //FavoriteScreen   favorite/
    composable(route = TestScreen.Favorite.createFormatRoute(root)) {// 每一个 composable 代表了一个页面，route 代表这个页面的地址
        FavoritesScreen(openProvinceScreen = {
            navController.navigate(route = TestScreen.Province.createFormatRoute(root))//这里的 route 和目标 composable 中的 route 对应
        })
    }

    //ProvinceScreen
    composable(route = TestScreen.Province.createFormatRoute(root)) {
        ProvinceScreen(openCityScreen = { provinceId ->
            navController.navigate(
                route = TestScreen.City.createValuesRoute(root, provinceId)//favorite/city/12345678
            )
        })
    }

    //CityScreen 页面需要 provinceId
    composable(route = TestScreen.City.createFormatRoute(root)) { backStackEntry -> //favorite/city/{provinceId}
        val provinceId = backStackEntry.arguments?.getString("provinceId")
        requireNotNull(provinceId) { "ProvinceScreen -> CityScreen: provinceId wasn't found!" }
        CityScreen(provinceId = provinceId, openFavoriteScreen = { cityId ->
            //cityId传到 ViewModel, FavoriteScreen在从 ViewModel中获取
            navController.popBackStack(
                route = TestScreen.Favorite.createFormatRoute(root),
                inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
            )
        })
    }

}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWeatherNavGraph(navController: NavController) {
    addWeather(Screen.Weather)
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWeather(root: Screen) {
    composable(Screen.Weather.route) {
        WeatherScreen()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMoreNavGraph(navController: NavController) {
    addMore(Screen.More)
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMore(root: Screen) {
    composable(Screen.More.route) {
        MoreScreen()
    }
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviEnterTransition(
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
private fun AnimatedContentScope<*>.defaultTiviExitTransition(
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

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
