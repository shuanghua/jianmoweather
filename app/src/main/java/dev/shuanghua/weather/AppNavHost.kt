package dev.shuanghua.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.shuanghua.ui.city.CityScreenDestination
import dev.shuanghua.ui.city.cityGraph
import dev.shuanghua.ui.district.DistrictDestination
import dev.shuanghua.ui.district.districtGraph
import dev.shuanghua.ui.favorite.FavoriteDestination
import dev.shuanghua.ui.favorite.favoriteGraph
import dev.shuanghua.ui.more.moreGraph
import dev.shuanghua.ui.province.ProvinceDestination
import dev.shuanghua.ui.province.provinceGraph
import dev.shuanghua.ui.station.StationDestination
import dev.shuanghua.ui.station.stationGraph
import dev.shuanghua.ui.weather.WeatherDestination
import dev.shuanghua.ui.weather.weatherGraph
import timber.log.Timber

/**
 * 传值和导航都在此处处理
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        favoriteGraph(
            navigateToProvinceScreen = {
                navController.navigate(ProvinceDestination.route)
            },
            nestedGraphs = {
                provinceGraph(
                    onBackClick = { navController.popBackStack() },
                    navigateToCityScreen = { provinceId, provinceName ->
                        navController.navigate("${CityScreenDestination.route}/$provinceId/$provinceName")
                    }
                )
                cityGraph(
                    onBackClick = { navController.popBackStack() },
                    navigateToFavoriteScreen = {
                        navController.popBackStack(  // cityId 传到 ViewModel, FavoriteScreen 在从 ViewModel 中获取
                            route = FavoriteDestination.destination, // favorite
                            inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
                        )
                    }
                )
            }
        )

        weatherGraph(
            navigateToAirDetails = {},
            navigateToDistrictScreen = { cityId, obtId ->
                navController.navigate("${DistrictDestination.route}/$cityId/$obtId")
            },
            nestedGraphs = {
                districtGraph(
                    onBackClick = { navController.popBackStack() },
                    navigateToStationScreen = { districtName ->
                        navController.navigate("${StationDestination.route}/$districtName")
                    }
                )
                stationGraph(
                    onBackClick = { navController.popBackStack() },
                    navigateToWeatherScreen = {
                        //弹出式返回受限于导航API，不能直接传值，推荐使用数据库或者datastore
                        //isLocation本身存数据库比较好，方便下次重新进入首页是判断是否为定位页面
                        navController.popBackStack(  // cityId 传到 ViewModel, FavoriteScreen 在从 ViewModel 中获取
                            route = WeatherDestination.destination,
                            inclusive = false // 如果为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
                        )
                    }
                )


            } // 区县页面 -> 街道站点页面
        )

        moreGraph(
//            navigateToThemeMode = {},
//            nestedGraphs = {}
        )
    }
}
