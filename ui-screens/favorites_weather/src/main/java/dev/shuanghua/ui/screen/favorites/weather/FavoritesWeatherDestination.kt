package dev.shuanghua.ui.screen.favorites.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.shuanghua.ui.core.navigation.AppNavigationDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi

object FavoritesWeatherDestination : AppNavigationDestination {
    override val route = "favorite_weather_route"
    override val destination = "favorite_weather_destination"
}

// 格式：favorite_weather_route/{provinceId}/{provinceName}
// 传值: "favorite_weather_route/123456/北京"
// navController.navigate(route) 的route是传值route
// composable(route)的route是格式route

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.favoriteWeatherScreenGraph(
    onBackClick: () -> Unit,
    openAirDetails: () -> Unit
) {

    navigation(
        route = FavoritesWeatherDestination.route,
        startDestination = FavoritesWeatherDestination.destination
    ) {
        composable(route = FavoritesWeatherDestination.destination) {//省份页面的地址
            FavoritesWeatherScreen(//接收页面的回调事件，因为回调逻辑涉及页面跳转需要 navController 对象，所以继续将事件传递到上游处理
                onBackClick = onBackClick,
                openAirDetails = openAirDetails
            )
        }
    }
}