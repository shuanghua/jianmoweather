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

//    @Inject
//    lateinit var viewModel: MainActivityViewModel
//    viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

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


		//  当创建一个带有参数的 ViewModel 时，需要去使用 ViewModelProvider.Factory 这个接口
		//  如果不想实现这个接口，那么就使用依赖注入库提供的对应方式,
		//  如 Hilt 需要在 ChildViewModel 类上使用 @HiltViewModel 注解来标识，
		//  并且使用 @Inject 来标记构造函数，以及要实现其中的参数的创建,
		//  然后在Activity中使用  private lateinit var viewModel: MainActivityViewModel 创建

		WindowCompat.setDecorFitsSystemWindows(window, false)// 让应用界面能显示在系统栏下面
		setContent {
			// val uiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
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
		when {
			permissions.all { it.value } -> {
				openMainScreen = true
			}

			// 对于进一步申请精确位置的询问，请将该逻辑移动到 SettingsScreen 中，然后调用系统应用设置界面
			permissions.any {// 勾选不再询问
				!it.value && !locationPermissionState.shouldShowRationale
			} -> {
				openMainScreen = true // 允许用户使用大概位置以及不使用任何位置权限也可进入 App
			}

			else -> {
				openMainScreen = true
			}
		}
	}

	// App 启动时立刻检查权限
	// // locationPermissionState.shouldShowRationale = true 意味着可以再次申请
	LaunchedEffect(key1 = locationPermissionState) {
		if (locationPermissionState.allPermissionsGranted) {
			// 权限已授予
			openMainScreen = true
		} else if (locationPermissionState.shouldShowRationale) {
			// 用户上次拒绝了某一个权限，虽然可以再次申请，但这里允许用户直接进入首页
			openMainScreen = true
		} else {
			// 用户第一次申请权限，或者不能再次申请（如果是不能再次唤起系统权限dialog，则需要引导用户去设置页面打开权限）
			shouldShowRequest = true // 这里只考虑用户第一次申请权限，引导用户去设置页面以后会放到 SettingsScreen 页面
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
//				shouldShowRequest = false
//				locationPermissionState.launchMultiplePermissionRequest() //请求方式 1
				// 申请权限触发代码，不管拒绝还是允许，都可以进入 app 首页 ，只是定位成功和失败的处理不同
				// 因此这里就不在这里处理：当只允许大概位置，而没有精确位置时，需要进一步申请精确位置权限的情况
				launcher.launch( // 请求方式 2
					listOf(
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION
					).toTypedArray()
				)
			}
		)
	}


//	if (shouldShowRationale) {
//		Timber.e("进一步询问授权精确位置")
//		PermissionDialog(
//			titleText = "需要精确位置权限",
//			leftButtonText = "不允许",
//			rightButtonText = "允许精确位置",
//			contentText = "精确定位:提高天气数据的准确度",
//			confirmButtonAction = {
//				shouldShowRationale = false // 关闭当前 dialog
//				launcher.launch(
//					listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray()
//				)
////				locationPermissionState.launchMultiplePermissionRequest() // 显示系统 dialog
//			}
//		)
//	}
//
//	if (shouldShowSettings) {
//		Timber.e("去设置打开位置权限")
//		PermissionDialog(
//			titleText = "需要位置权限",
//			leftButtonText = "我想先体验一下",
//			rightButtonText = "去设置",
//			contentText = "应用很依赖定位功能，请授予定位权限",
//			confirmButtonAction = {
//				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//					data = Uri.fromParts("package", context.packageName, null)
//				}
////				context.startActivity(intent)
//
//			}
//		)
//	}
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