package dev.shuanghua.weather.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.shuanghua.weather.R
import dev.shuanghua.weather.Screen
import dev.shuanghua.weather.appScreenNavigation


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()
    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        Screen.Weather.route -> bottomBarState.value = true
        Screen.Favorite.route -> bottomBarState.value = true
        Screen.More.route -> bottomBarState.value = true
        else -> bottomBarState.value = false
    }

    Scaffold(
        bottomBar = { JianMoBottomBar(navController, bottomBarState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Weather.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            appScreenNavigation(navController)
        }
    }
}

/**
 * 底部 tab 点击切换页面
 * Modifier.navigationBarsPadding() :
 * 如果父 Layout 不设置，子View设置了，则子View会让父Layout膨胀变大（父Layout高度增加），但父Layout依然占据systemBar空间
 * 如果父 Layout 设置了，子 View 不设置，则子view并不会去占据systemBar空间
 * 总结：子 View 永远不会改变 父Layout的空间位置，但可以更改父Layout的大小
 */
@Composable
fun JianMoBottomBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Surface(tonalElevation = 2.dp) {//tonalElevation 改变 surfaceColor 的深浅
            val currentSelectedItem by navController.currentScreenAsState()
            MainScreenNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected: Screen ->
                    navController.navigate(route = selected.route) {
                        launchSingleTop = true
                        restoreState = true
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                    }
                }
            )
        }
    }
}

/**
 * selectedNavigation：要选中的item
 */
@Composable
internal fun MainScreenNavigation(
    selectedNavigation: Screen, //传入 当前正在选中的 item
    onNavigationSelected: (Screen) -> Unit, //传出 用户点击之后的新 item
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        // Material 3
        // navigationBarsPadding 远离导航栏,
        // 但下层的 Surface或Box 依然填充占用导航栏空间
        // 利用这个方法,另外设置导航栏为透明,就可以设置出统一好看的 ui
        modifier = modifier.navigationBarsPadding(),
        containerColor = Color.Transparent,
    ) {
        MainScreenNavItems.forEach { item: MainScreenNavItem ->
            NavigationBarItem(
                label = { Text(text = stringResource(item.labelResId)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) },
                icon = {
                    MainScreenNavItemIcon(
                        item = item,
                        selected = selectedNavigation == item.screen
                    )
                }
            )
        }
    }
}

/**
 * 重写监听事件，让选中的页面具有 State 特性
 */
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Weather) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Weather.route } -> {
                    selectedItem.value = Screen.Weather
                }
                destination.hierarchy.any { it.route == Screen.Favorite.route } -> {
                    selectedItem.value = Screen.Favorite
                }
                destination.hierarchy.any { it.route == Screen.More.route } -> {
                    selectedItem.value = Screen.More
                }
            }
        }
        addOnDestinationChangedListener(listener)
        onDispose { removeOnDestinationChangedListener(listener) }
    }
    return selectedItem
}

/**
 * 设置 bottomBar 图标和文字
 * 用 class 来表示 navigationItem, 每个 navigationItem 就是一个类，类中包含 item 图标和标题以及对应的 Screen route
 */
private sealed class MainScreenNavItem(
    val screen: Screen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int
) {
    class ResourceIcon( //普通图片
        screen: Screen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
        @DrawableRes val selectedIconResId: Int? = null
    ) : MainScreenNavItem(screen, labelResId, contentDescriptionResId)

    class VectorIcon( //矢量图片
        screen: Screen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null
    ) : MainScreenNavItem(screen, labelResId, contentDescriptionResId)
}

private val MainScreenNavItems = listOf(// 收集 NavigationItem Class, 并设置对应 screen 、图标和文字
    MainScreenNavItem.VectorIcon(
        screen = Screen.Favorite,
        labelResId = R.string.favorite,
        contentDescriptionResId = R.string.favorite,
        iconImageVector = Icons.Outlined.Favorite,
        selectedImageVector = Icons.Default.Favorite
    ),
    MainScreenNavItem.VectorIcon(
        screen = Screen.Weather,
        labelResId = R.string.weather,
        contentDescriptionResId = R.string.weather,
        iconImageVector = Icons.Outlined.Home,
        selectedImageVector = Icons.Default.Home
    ),
    MainScreenNavItem.VectorIcon(
        screen = Screen.More,
        labelResId = R.string.more,
        contentDescriptionResId = R.string.more,
        iconImageVector = Icons.Outlined.Menu,
        selectedImageVector = Icons.Default.Menu
    )
)

/**
 * 根据 NavigationItem 设置的图标类型来确定的载入显示图标
 */
@Composable
private fun MainScreenNavItemIcon(item: MainScreenNavItem, selected: Boolean) {
    val painter = when (item) {
        is MainScreenNavItem.ResourceIcon -> painterResource(item.iconResId)
        is MainScreenNavItem.VectorIcon -> rememberVectorPainter(item.iconImageVector)
    }

    val selectedPainter = when (item) {
        is MainScreenNavItem.ResourceIcon -> item.selectedIconResId?.let {
            painterResource(
                it
            )
        }
        is MainScreenNavItem.VectorIcon -> item.selectedImageVector?.let {
            rememberVectorPainter(it)
        }
    }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(item.contentDescriptionResId)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = stringResource(item.contentDescriptionResId)
        )
    }
}

/**
 * 收缩后的Navigation ，只显示图标
 * 平板大屏设备
 */
@Composable
internal fun MainScreenNavigationRail(
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        NavigationRail(
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.systemBarsPadding()
        ) {
            MainScreenNavItems.forEach { item ->
                NavigationRailItem(
                    icon = {
                        MainScreenNavItemIcon(
                            item = item,
                            selected = selectedNavigation == item.screen
                        )
                    },
                    alwaysShowLabel = false,
                    label = { Text(text = stringResource(item.labelResId)) },
                    selected = selectedNavigation == item.screen,
                    onClick = { onNavigationSelected(item.screen) },
                )
            }
        }
    }
}