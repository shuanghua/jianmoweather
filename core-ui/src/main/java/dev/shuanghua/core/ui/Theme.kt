package dev.shuanghua.core.ui

import android.os.Build
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Light default theme color scheme
 */
private val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    outline = PurpleGray50
)

/**
 * Dark default theme color scheme
 */
private val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    outline = PurpleGray60
)

/**
 * Light Android theme color scheme
 */
private val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    outline = GreenGray50
)

/**
 * Dark Android theme color scheme
 */
private val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    outline = GreenGray60
)

//val JianMoWeatherLightTheme = lightColorScheme(
//    primary = Blue40,
//    onPrimary = Color.White,
//    primaryContainer = Blue90,
//    onPrimaryContainer = Blue10,
//    inversePrimary = Blue80,
//    secondary = DarkBlue40,
//    onSecondary = Color.White,
//
//    secondaryContainer = DarkBlue80,//BottomBar选中的背景色，不包括图标
//
//    onSecondaryContainer = DarkBlue10,
//    tertiary = Yellow40,
//    onTertiary = Color.White,
//    tertiaryContainer = Yellow90,
//    onTertiaryContainer = Yellow10,
//    error = Red40,
//    onError = Color.White,
//    errorContainer = Red90,
//    onErrorContainer = Red10,
//    background = Grey99,
//    onBackground = Grey10,
//    surface = Grey99,
//    onSurface = Grey10,
//    inverseSurface = Grey20,
//    inverseOnSurface = Grey95,
//    surfaceVariant = BlueGrey90,
//    onSurfaceVariant = BlueGrey30,
//    outline = BlueGrey50
//)
//val JianMoWeatherDarkTheme = darkColorScheme(
//    primary = Blue80,
//    onPrimary = Blue20,
//    primaryContainer = Blue30,
//    onPrimaryContainer = Blue90,
//    inversePrimary = Blue40,
//    secondary = DarkBlue80,
//    onSecondary = DarkBlue20,
//
//    secondaryContainer = DarkBlue30,
//
//    onSecondaryContainer = DarkBlue90,
//    tertiary = Yellow80,
//    onTertiary = Yellow20,
//    tertiaryContainer = Yellow30,
//    onTertiaryContainer = Yellow90,
//    error = Red80,
//    onError = Red20,
//    errorContainer = Red30,
//    onErrorContainer = Red90,
//    background = Grey10,
//    onBackground = Grey90,
//    surface = Grey10,
//    onSurface = Grey80,
//    inverseSurface = Grey90,
//    inverseOnSurface = Grey20,
//    surfaceVariant = BlueGrey30,
//    onSurfaceVariant = BlueGrey80,
//    outline = BlueGrey60
//)

@Composable
fun JianMoTheme(
    darkTheme: Int = 2,
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    androidTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val tm = when (darkTheme) {
        1 -> true
        2 -> false
        else -> isSystemInDarkTheme()
    }


    // Material V3 Compose View:
    // https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
    val sysUiController = rememberSystemUiController()

    SideEffect {
        // 在小米手机上需要分开设置
        sysUiController.setNavigationBarColor(color = Color.Transparent)
        sysUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !tm
        )
    }

    val appThemeScheme = when {
        dynamicColor && tm -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !tm -> dynamicLightColorScheme(LocalContext.current)

        androidTheme && tm -> DarkAndroidColorScheme
        androidTheme -> LightAndroidColorScheme
        tm -> DarkDefaultColorScheme
        else -> LightDefaultColorScheme
    }


    MaterialTheme(
        colorScheme = appThemeScheme,
        typography = JianMoTypography,
    ) {
        // TODO (M3): 当前版本 MaterialTheme 不提供 LocalIndication，当它提供时，请删除以下内容。
        val rippleIndication = rememberRipple() // M1
        CompositionLocalProvider(
//            LocalOverScrollConfiguration provides null,
            LocalIndication provides rippleIndication,
            content = content
        )
    }
}

@Immutable
data class BackgroundTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
    val primaryGradientColor: Color = Color.Unspecified,
    val secondaryGradientColor: Color = Color.Unspecified,
    val tertiaryGradientColor: Color = Color.Unspecified,
    val neutralGradientColor: Color = Color.Unspecified
)

val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }


@Composable
fun topBarForegroundColors() = TopAppBarDefaults.centerAlignedTopAppBarColors(
    containerColor = Color.Transparent,
    scrolledContainerColor = Color.Transparent,
    actionIconContentColor = MaterialTheme.colorScheme.onSurface
)

@Composable
fun topBarBackgroundColor(scrollBehavior: TopAppBarScrollBehavior): Color {
    val topBarBackgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    return topBarBackgroundColors.containerColor(
        scrollFraction = scrollBehavior.scrollFraction ?: 0f  //  离开顶部时设置为 surfaceColor, 否则使用默认
    ).value
}