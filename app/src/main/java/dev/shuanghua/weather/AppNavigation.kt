package dev.shuanghua.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.ui.city.CityListScreen
import dev.shuanghua.ui.favorite.FavoritesScreen
import dev.shuanghua.ui.more.MoreScreen
import dev.shuanghua.ui.province.ProvinceListScreen
import dev.shuanghua.ui.weather.WeatherScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * APP 下所有页面的 route
 */
sealed class Screen(val route: String) {
    /** [FavoritesScreen]   */
    object Favorite : Screen("screen_favorite")

    /** [WeatherScreen]     */
    object Weather : Screen("screen_weather")

    /** [MoreScreen]        */
    object More : Screen("screen_more")

    /** [ProvinceListScreen] */
    object Province : Screen("screen_province")

    /** [CityListScreen] */
    object CityList : Screen("screen_city/{provinceId}") {
        /**
         * 创建传值的 route
         */
        fun argsRoute(provinceId: String) = "screen_city/$provinceId"
    }
}

sealed class Module(val route: String) {
    object AddCity : Module("module_addcity")
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
fun NavGraphBuilder.appScreenNavigation(navController: NavController) {
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
        FavoritesScreen(openProvinceScreen = {
            navController.navigate(route = Module.AddCity.route)
        })
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
