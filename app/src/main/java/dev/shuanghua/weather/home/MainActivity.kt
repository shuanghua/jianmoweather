package dev.shuanghua.weather.home

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import dev.shuanghua.core.ui.AppBackground
import dev.shuanghua.core.ui.JianMoTheme

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)// 让应用界面能显示在系统栏下面
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        //  当创建一个带有参数的 ViewModel 时，需要自己再去 ViewModelProvider.Factory 这个接口
        //  如果不想自己实现这个接口，那么就使用 @HiltViewModel 来标识你的 ViewModel 类，
        //  并且使用 @Inject 来标记构造函数，以及要实现其中的参数的创建

        setContent {
//            JiamMoApp()
            CheckThemeMode(viewModel)
        }
    }
}

@Composable
fun JiamMoApp(){
    Surface(color = Color.Red, modifier = Modifier.fillMaxSize()) {
        Text(text = "jfepwojafoefu")
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CheckThemeMode(
    viewModel: MainActivityViewModel,
) {
    val themeModeUiState by viewModel.themeModeUiState.collectAsStateWithLifecycle()
    val darkThemeMode = when (themeModeUiState.tm) {
        0 -> true
        1 -> false
        else -> isSystemInDarkTheme()
    }
    JianMoTheme(darkTheme = darkThemeMode) {
        AppBackground {
            RequestLocationPermission()
        }
    }
}


/**
 * https://github.com/google/accompanist/blob/main/sample/src/main/java/com/google/accompanist/sample/permissions/RequestLocationPermissionsSample.kt
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestLocationPermission() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {//1.户点击允许权限时，2.上次已经允许了
        MainScreen()
    } else {
        val allPermissionRevoked =
            locationPermissionsState.permissions.size ==
                    locationPermissionsState.revokedPermissions.size

        val textToShow = if (!allPermissionRevoked) {
            "Thanks granted a permission"
        } else if (locationPermissionsState.shouldShowRationale) {
            "获取您的精确位置对于此应用程序很重要。请给我们很好的位置。谢谢：D "
        } else {
            "应用很依赖定位功能，请同意授予定位权限 \n[我们只使用到您的“定位地址”和“经纬度”]"
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
                    TextButton(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
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