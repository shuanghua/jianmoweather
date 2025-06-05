package dev.shuanghua.weather.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.shuanghua.weather.R
import dev.shuanghua.weather.navigation.AppNavHost
import dev.shuanghua.weather.navigation.FavoriteNavigation
import dev.shuanghua.weather.navigation.MoreNavigation
import dev.shuanghua.weather.navigation.WeatherNavigation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
	val navController = rememberNavController()
	val bottomBarState = rememberSaveable { mutableStateOf(true) }
	val navBackStackEntry by navController.currentBackStackEntryAsState()

	// 在哪些页面展示 BottomNavigationBar
	bottomBarState.value = when {
		navBackStackEntry?.destination?.hierarchy?.any {
			it.route == WeatherNavigation::class.qualifiedName ||
					it.route == FavoriteNavigation::class.qualifiedName ||
					it.route == MoreNavigation::class.qualifiedName
		} == true -> true

		else -> false
	}

	Scaffold(
		containerColor = Color.Transparent,
		contentColor = MaterialTheme.colorScheme.onBackground,
		contentWindowInsets = WindowInsets(0, 0, 0, 0),
		bottomBar = {
			AppBottomNavBar(navController, bottomBarState)
		}
	) { innerPadding ->
		Row(  // 左右布局的目的是为了日后适配平板设备时方便调整 BottomBar 位置
			Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.consumeWindowInsets(innerPadding)
				.windowInsetsPadding(
					WindowInsets.safeDrawing.only(
						WindowInsetsSides.Horizontal
					)
				)
		) {
			AppNavHost(
				navController = navController,
			)
		}
	}

}

/**
 * 底部 tab 点击切换页面
 * Modifier.navigationBarsPadding() :
 * 如果父 Layout 不设置，子View设置了，则子 View 会让父 Layout 膨胀变大（父 Layout 高度增加），但父Layout依然占据systemBar空间
 * 如果父 Layout 设置了，子 View 不设置，则子 view 并不会去占据systemBar空间
 * 总结：子 View 永远不会改变 父Layout的空间位置，但可以更改父Layout的大小
 */
@Composable
fun AppBottomNavBar(
	navController: NavController,
	bottomBarState: MutableState<Boolean>,
) {
	AnimatedVisibility(
		visible = bottomBarState.value,
		enter = slideInVertically(initialOffsetY = { it }),
		exit = slideOutVertically(targetOffsetY = { it })
	) {
		val currentSelectedItem by navController.currentScreenAsState() // 由 remember 处理之后
		MaterialNavigation(
			selectedNavigation = currentSelectedItem,
			onNavigateToBottomBarDestination = { item: AppNavItem ->
				navController.navigate(route = item.route) {
					popUpTo(navController.graph.findStartDestination().id) {
						saveState = true
					}
					launchSingleTop = true
					restoreState = true
				}
			}
		)
	}
}

/**
 * 给BottomNavBar设置监听、图标、文字等
 */
@Composable
internal fun MaterialNavigation(
	selectedNavigation: Any, //传入 当前正在选中的 item
	onNavigateToBottomBarDestination: (AppNavItem) -> Unit, //传出 用户点击之后的新 item
) {
	NavigationBar(
		// Material 3
		// navigationBarsPadding 远离导航栏,
		// 但下层的 Surface或Box 依然填充占用导航栏空间
		// 利用这个方法,另外设置导航栏为透明,就可以设置出统一好看的 ui
		//modifier = modifier.navigationBarsPadding(),
		containerColor = Color.Transparent
	) {
		bottomBarItems.forEach { item: AppNavItem ->
			NavigationBarItem(
				label = { Text(text = stringResource(item.labelResId)) },
				selected = selectedNavigation == item.route,
				onClick = { onNavigateToBottomBarDestination(item) },
				icon = {
					BottomNavItemIcon(
						item = item,
						selected = selectedNavigation == item.route
					)
				}
			)
		}
	}
}

/**
 * 重写监听事件，让选中的页面具有 State 特性
 *
 */
@Composable
private fun NavController.currentScreenAsState(): State<Any> {
	val selectedItem = remember { mutableStateOf<Any>(WeatherNavigation) }
	DisposableEffect(this) {
		val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
			when {
				//当导航的 route 等于 bottomBar 对应的绑定的route 时
				destination.hierarchy.any { it.route == WeatherNavigation::class.qualifiedName }
					-> selectedItem.value = WeatherNavigation


				destination.hierarchy.any { it.route == FavoriteNavigation::class.qualifiedName }
					-> selectedItem.value = FavoriteNavigation


				destination.hierarchy.any { it.route == MoreNavigation::class.qualifiedName }
					-> selectedItem.value = MoreNavigation

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
sealed class AppNavItem(
	val route: Any,
	@StringRes val labelResId: Int,
	@StringRes val contentDescriptionResId: Int,
) {
	class ResourceIcon<T : Any>(
		//普通图片
		route: T,
		@StringRes labelResId: Int,
		@StringRes contentDescriptionResId: Int,
		@DrawableRes val iconResId: Int,
		@DrawableRes val selectedIconResId: Int? = null,
	) : AppNavItem(route, labelResId, contentDescriptionResId)

	class VectorIcon<T : Any>(
		//矢量图片
		route: T,
		@StringRes labelResId: Int,
		@StringRes contentDescriptionResId: Int,
		val iconImageVector: ImageVector,
		val selectedImageVector: ImageVector? = null,
	) : AppNavItem(route, labelResId, contentDescriptionResId)
}

private val bottomBarItems = listOf(// 收集 NavigationItem, 并设置对应 screen 、图标和文字
	AppNavItem.VectorIcon(
		route = FavoriteNavigation,
		labelResId = R.string.favorite,
		contentDescriptionResId = R.string.favorite,
		iconImageVector = Icons.Outlined.Favorite,
		selectedImageVector = Icons.Default.Favorite
	),
	AppNavItem.VectorIcon(
		route = WeatherNavigation,
		labelResId = R.string.weather,
		contentDescriptionResId = R.string.weather,
		iconImageVector = Icons.Outlined.Home,
		selectedImageVector = Icons.Default.Home
	),
	AppNavItem.VectorIcon(
		route = MoreNavigation,
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
private fun BottomNavItemIcon(item: AppNavItem, selected: Boolean) {
	val painter = when (item) {
		is AppNavItem.ResourceIcon<*> -> painterResource(item.iconResId)
		is AppNavItem.VectorIcon<*> -> rememberVectorPainter(item.iconImageVector)
	}

	val selectedPainter = when (item) {
		is AppNavItem.ResourceIcon<*> -> item.selectedIconResId?.let {
			painterResource(
				it
			)
		}

		is AppNavItem.VectorIcon<*> -> item.selectedImageVector?.let {
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

///**
// * 收缩后的Navigation ，只显示图标
// * 平板大屏设备
// */
//@Composable
//internal fun MainScreenNavigationRail(
//    selectedNavigation: String,
//    onNavigationSelected: (String) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Surface(
//        color = MaterialTheme.colorScheme.surface,
//        modifier = modifier,
//    ) {
//        NavigationRail(
//            contentColor = MaterialTheme.colorScheme.onSurface,
//            modifier = Modifier.systemBarsPadding()
//        ) {
//            BottomNavItems.forEach { item ->
//                NavigationRailItem(
//                    icon = {
//                        MainScreenNavItemIcon(
//                            item = item,
//                            selected = selectedNavigation == item.screen
//                        )
//                    },
//                    alwaysShowLabel = false,
//                    label = { Text(text = stringResource(item.labelResId)) },
//                    selected = selectedNavigation == item.screen,
//                    onClick = { onNavigationSelected(item.screen) },
//                )
//            }
//        }
//    }
//}