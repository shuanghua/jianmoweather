package jianmoweather.module.common_ui_compose

sealed class Screen(val route: String) {
    object Favorite : Screen("favorite")
    object Weather : Screen("weather")
    object More : Screen("more")
}