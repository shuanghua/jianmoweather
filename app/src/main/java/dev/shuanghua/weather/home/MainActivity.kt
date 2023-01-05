package dev.shuanghua.weather.home

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import dev.shuanghua.core.ui.AppBackground
import dev.shuanghua.core.ui.JianMoTheme
import dev.shuanghua.datastore.model.ThemeConfig.*
import dev.shuanghua.module.ui.compose.demo.PullRefreshSample
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var viewModel: MainActivityViewModel
//    viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }


        //  当创建一个带有参数的 ViewModel 时，需要自己再去 ViewModelProvider.Factory 这个接口
        //  如果不想自己实现这个接口，那么就使用 @HiltViewModel 来标识你的 ViewModel 类，
        //  并且使用 @Inject 来标记构造函数，以及要实现其中的参数的创建,然后在Activity中使用  private lateinit var viewModel: MainActivityViewModel 创建

        WindowCompat.setDecorFitsSystemWindows(window, false)// 让应用界面能显示在系统栏下面
        setContent {
            // val uiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
            JianMoTheme(darkTheme = shouldUseDarkTheme(uiState)) {
//                PullRefreshSample()

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
    MainActivityUiState.Loading -> isSystemInDarkTheme()
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