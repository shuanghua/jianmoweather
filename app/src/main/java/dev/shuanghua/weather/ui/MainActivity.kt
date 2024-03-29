package dev.shuanghua.weather.ui

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import org.koin.android.java.KoinAndroidApplication
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.KoinContext

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

/**
 * https://github.com/google/accompanist/blob/main/sample/src/main/java/com/google/accompanist/sample/permissions/RequestLocationPermissionsSample.kt
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestLocationPermission() {
	val appPermissionList = rememberMultiplePermissionsState(
		listOf(
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION
		)
	)

	if (appPermissionList.allPermissionsGranted) {//1.户点击允许权限时，2.上次已经允许了
		MainScreen()
	} else {
		val allPermissionRevoked =
			appPermissionList.permissions.size == appPermissionList.revokedPermissions.size

		val textToShow = if (!allPermissionRevoked) {//权限需要在设置中手动开启
			"Thanks granted a permission"
		} else if (appPermissionList.shouldShowRationale) {//向用户解释app使用权限的目的
			"精确的经纬度有助于就近的观测站点"
		} else {
			"应用很依赖定位功能，请同意授予定位权限 \n[仅用到您的“市、区或县”及“经纬度”]"
		}

		val buttonText: String = if (!allPermissionRevoked) {
			"允许精确定位"
		} else {
			"请求权限"
		}

		val activity = (LocalContext.current as? Activity)
		val openDialog = remember { mutableStateOf(true) }

		if (openDialog.value) {
			AlertDialog(
				onDismissRequest = {
					openDialog.value = false
				},
				dismissButton = {
					TextButton(onClick = { activity?.finish() }) {
						Text(text = "退出应用")
					}
				},
				confirmButton = {
					TextButton(onClick = { appPermissionList.launchMultiplePermissionRequest() }) {
						Text(text = buttonText)
					}
				},
				title = { Text(text = "需要位置权限") },
				text = {
					Text(text = textToShow)
				},
			)
		}

		Spacer(modifier = Modifier.height(8.dp))
//        SideEffect {
//            locationPermissionsState.launchMultiplePermissionRequest()
//        }
	}
}