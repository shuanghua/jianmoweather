package dev.shuanghua.ui.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.shuanghua.weather.data.android.model.ThemeConfig


@Composable
fun ChangeThemeDialog(
    themeSettings: ThemeSettings,
    onChangeThemeConfig: (ThemeConfig) -> Unit,
    onDismiss: () -> Unit, // dialog关闭
) {
    AlertDialog(
        title = { Text(text = "主题选择") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "确定")
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SingleChoiceItem(
                    text = "跟随系统",
                    selected = themeSettings.themeConfig == ThemeConfig.FOLLOW_SYSTEM,
                    onClick = {
                        onChangeThemeConfig(ThemeConfig.FOLLOW_SYSTEM)
                    })
                SingleChoiceItem(
                    text = "白色",
                    selected = themeSettings.themeConfig == ThemeConfig.LIGHT,
                    onClick = { onChangeThemeConfig(ThemeConfig.LIGHT) }
                )
                SingleChoiceItem(
                    text = "暗色",
                    selected = themeSettings.themeConfig == ThemeConfig.Dark,
                    onClick = { onChangeThemeConfig(ThemeConfig.Dark) }
                )
            }
        }
    )
}