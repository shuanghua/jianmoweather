package dev.shuanghua.weather.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.shuanghua.ui.core.compose.AppBackground
import dev.shuanghua.ui.core.compose.JianMoTheme
import dev.shuanghua.weather.data.android.model.ThemeConfig.Dark
import dev.shuanghua.weather.data.android.model.ThemeConfig.FOLLOW_SYSTEM
import dev.shuanghua.weather.data.android.model.ThemeConfig.LIGHT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

	private val viewModel: MainViewModel by viewModel()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Error)

		lifecycleScope.launch {
			lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.uiState
					.onEach {
						uiState = it
					}
					.collect()
			}
		}

		WindowCompat.setDecorFitsSystemWindows(window, false)// 让应用界面能显示在系统栏下面
		setContent {
			JianMoTheme(darkTheme = shouldUseDarkTheme(uiState)) {
				AppBackground {
					RequestLocationPermission()
				}
			}
		}
	}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestLocationPermission() {
	var openMainScreen by remember { mutableStateOf(false) }
	var shouldShowRequest by remember { mutableStateOf(false) }
	val locationPermissionState = rememberMultiplePermissionsState(
		listOf(
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION
		)
	)

	if (openMainScreen) {
		shouldShowRequest = false
		MainScreen()
	}

	// 处理 settings 页面返回后的逻辑
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { permissions ->
		openMainScreen = when {
			permissions.all { it.value } -> true
			// 勾选不再询问后  允许用户以大概位置或不使用任何位置权限也可进入 App
			permissions.any { !it.value && !locationPermissionState.shouldShowRationale } -> true
			else -> true
		}
	}

	// App 启动时立刻检查权限
	// locationPermissionState.shouldShowRationale = true 意味着可以再次申请
	LaunchedEffect(key1 = locationPermissionState) {
		when {
			// 权限已授予 + 用户上次拒绝了某一个权限，但依然可以再次申请， 本 App 但这里不需要再次申请，让用户可以直接进入首页
			locationPermissionState.allPermissionsGranted or
					locationPermissionState.shouldShowRationale  -> openMainScreen = true
			// 用户第一次申请权限，或者不能再次申请
			// 这里只考虑用户第一次申请权限
			// 如果是后续不能调起系统 dialog 了，需要单独记录标记， 然后引导用户去设置页面， 该功能以后会放到 SettingsScreen 页面
			else -> shouldShowRequest = true
		}
	}

	if (shouldShowRequest) {
		PermissionDialog(
			titleText = "需要位置权限",
			leftButtonText = "我想先体验",
			rightButtonText = "请求权限",
			contentText = "应用功能很依赖精确位置权限，请授予精确定位权限\n[省份 区县 及经纬度]",
			dismissButtonAction = { openMainScreen = true },
			confirmButtonAction = {
				// 申请权限触发代码，不管拒绝还是允许，都可以进入 app 首页 ，只是定位成功和失败的处理不同
				// 因此就不在这里处理：当只允许大概位置，而没有精确位置时，需要进一步申请精确位置权限的情况
				launcher.launch(
					listOf(
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION
					).toTypedArray()
				)
			}
		)
	}
}

@Composable
fun PermissionDialog(
	titleText: String,
	leftButtonText: String,
	rightButtonText: String,
	contentText: String,
	confirmButtonAction: () -> Unit = {},
	dismissButtonAction: () -> Unit = {},
) {
	AlertDialog(
		onDismissRequest = { },
		dismissButton = {
			TextButton(onClick = {
				dismissButtonAction()
			}) {
				Text(text = leftButtonText)
			}
		},
		confirmButton = {
			TextButton(onClick = confirmButtonAction) {
				Text(text = rightButtonText)
			}
		},
		title = { Text(text = titleText) },
		text = { Text(text = contentText) }
	)
}

@Composable
private fun shouldUseDarkTheme(
	uiState: MainActivityUiState,
): Boolean = when (uiState) {
	MainActivityUiState.Error -> isSystemInDarkTheme()
	is MainActivityUiState.Success -> when (uiState.themeSettings.themeConfig) {
		FOLLOW_SYSTEM -> isSystemInDarkTheme()
		LIGHT -> false
		Dark -> true
	}
}