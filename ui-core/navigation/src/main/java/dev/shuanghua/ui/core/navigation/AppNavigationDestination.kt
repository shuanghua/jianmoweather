package dev.shuanghua.ui.core.navigation


//包括 api libs.hilt.compose.navigation
interface AppNavigationDestination {
    val route: String
    val destination: String
}