package com.moshuanghua.jianmoweather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import jianmoweather.favorite.city.CityScreen
import jianmoweather.favorite.province.ProvinceScreen
import jianmoweather.home.favorite.FavoriteScreen
import jianmoweather.home.favorite.FavoritesScreen
import jianmoweather.module.common_ui_compose.Screen


@ExperimentalAnimationApi
fun NavGraphBuilder.favoriteNavGraph(navController: NavController) {
    composable(route = FavoriteScreen.route()) {// 当前页面地址  favorite
        FavoritesScreen(
            openProvinceScreen = {
                navController.navigate(
                    route = "${FavoriteScreen.route()}/add_city" //  别的页面地址(目标页面地址)
                )
            }
        )
    }

    navigation(
        route = "${FavoriteScreen.route()}/add_city",//导航模块的 route 地址  favorite
        startDestination = ProvinceScreen.route(Screen.Favorite)// 导航模块中的第一个默认显示页面地址
    ) {
        navigationProvinceAndCity(Screen.Favorite, navController)
    }
}


/**
 * 导航模块:
 * 省份和城市页面组成的模块
 */
@ExperimentalAnimationApi
internal fun NavGraphBuilder.navigationProvinceAndCity(
    root: Screen,
    navController: NavController
) {
    composable(route = ProvinceScreen.route(root)) {
        ProvinceScreen(openCityScreen = { provinceId ->
            navController.navigate(
                // favorite/city/12345678
                // favorite/city/{provinceId}
                route = CityScreen.argsRoute(root, provinceId)
            )
        })
    }

    composable(route = CityScreen.argsRoute(root)) { backStackEntry ->  //favorite/city/{provinceId}
        val provinceId = backStackEntry.arguments?.getString("provinceId")
        requireNotNull(provinceId) { "ProvinceScreen -> CityScreen: provinceId wasn't found!" }
        CityScreen(provinceId = provinceId, openFavoriteScreen = {
            navController.popBackStack( // cityId 传到 ViewModel, FavoriteScreen 在从 ViewModel 中获取
                route = FavoriteScreen.route(),// favorite
                inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
            )
        })
    }
}