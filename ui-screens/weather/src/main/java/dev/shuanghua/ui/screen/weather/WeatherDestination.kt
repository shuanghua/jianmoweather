package dev.shuanghua.ui.screen.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.ExperimentalCoroutinesApi

// 格式：weather_route/{provinceId}/{provinceName}
// 传值: "weather_route/123456/北京"

const val weatherNavigation = "weather_navigation"
const val weatherRoute = "weather_route"

/**
 * Weather 页面导航参数处理
 * Weather 页面打开别的页面事件
 * 打开到空气质量页面
 * 打开到站点页面
 * 空气页面和站点页面内的导航事件
 */
fun NavGraphBuilder.weatherScreen(
    openAirDetails: (String) -> Unit,
    openDistrictList: (String, String) -> Unit,
    nestedGraphs: () -> Unit
) {
    // bottomBar 导航先找到 navigation-route ,发现 navigation 的页面属性 startDestination 是 WeatherDestination.destination
    // 再根据 WeatherDestination.destination 值去匹配 composable 中的 route
    navigation(
        route = weatherNavigation,
        startDestination = weatherRoute
    ) {
        composable(route = weatherRoute) { // 省份页面的地址
            WeatherRoute( // 接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
                openAirDetails = openAirDetails,
                navigateToDistrictScreen = openDistrictList
            )
        }
        nestedGraphs() //Weather 子页面的导航 如区县页面 -> 街道站点页面
    }
}