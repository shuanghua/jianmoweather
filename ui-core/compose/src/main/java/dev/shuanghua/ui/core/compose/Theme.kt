package dev.shuanghua.ui.core.compose

import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import timber.log.Timber

/**
 * Light default theme color scheme
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
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
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
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
@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
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
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
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

@Composable
fun JianMoTheme(
	darkTheme: Boolean = true,
	dynamicColor: Boolean = true,
	androidTheme: Boolean = true,
	content: @Composable () -> Unit,
) {

	// Material V3 Compose View:
	// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
	val sysUiController = rememberSystemUiController()

	SideEffect {
		// 在小米手机上需要分开设置
		sysUiController.setNavigationBarColor(color = Color.Transparent)
		sysUiController.setStatusBarColor(
			color = Color.Transparent,
			darkIcons = !darkTheme
		)
	}

	Timber.tag("Theme").d("Theme-->>$androidTheme + $darkTheme")

	val colorScheme = when {
		dynamicColor -> {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				val context = LocalContext.current
				if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
			} else {
				if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
			}
		}
		// 小米手机系统设置为深色 + app亮色时 ，显示有 bug
		androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
		else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
	}

	val defaultBackgroundTheme = BackgroundTheme(
		color = colorScheme.surface,
		tonalElevation = 2.dp
	)
	val backgroundTheme = when {
		dynamicColor -> defaultBackgroundTheme
		androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
		else -> defaultBackgroundTheme
	}

	CompositionLocalProvider(
		LocalBackgroundTheme provides backgroundTheme
	) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = JianMoTypography,
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
	val neutralGradientColor: Color = Color.Unspecified,
)

val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }

val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)