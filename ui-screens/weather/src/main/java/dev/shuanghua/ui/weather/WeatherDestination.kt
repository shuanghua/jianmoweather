package dev.shuanghua.ui.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.ui.core.navigation.AppNavigationDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi

object WeatherDestination : AppNavigationDestination {
    override val route = "weather_route"
    override val destination = "weather_destination"
    const val obtId = "obtId"
    const val isLocation = "isLocation"
}

// 格式：weather_route/{provinceId}/{provinceName}
// 传值: "weather_route/123456/北京"
// navController.navigate(route) 的route是传值route
// composable(route)的route是格式route

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.weatherScreenGraph(
    navigateToAirDetails: () -> Unit,
    navigateToDistrictScreen: (String, String) -> Unit,
    nestedGraphs: () -> Unit
) {
    // bottomBar 导航先找到 navigation.route ,发现 navigation 的页面属性 startDestination 是 WeatherDestination.destination
    // 再根据 WeatherDestination.destination 值去匹配 composable 中的 route
    navigation(
        route = WeatherDestination.route,
        startDestination = WeatherDestination.destination
    ) {
        composable(route = WeatherDestination.destination) {//省份页面的地址
            WeatherScreen(//接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
                openAirDetails = navigateToAirDetails,
                navigateToDistrictScreen = navigateToDistrictScreen
            )
        }
        nestedGraphs() //Weather 子页面的导航 如区县页面 -> 街道站点页面
    }
}