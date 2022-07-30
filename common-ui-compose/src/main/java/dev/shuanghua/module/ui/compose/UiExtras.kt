package dev.shuanghua.module.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionDialog(
    description: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = "关闭") }
        }
    )
}

@Composable
fun JianMoLazyRow(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
fun ThemeModeDialog(
    currentThemeMode: Int,
    onDismiss: () -> Unit = {},
    onThemeModeChange: (String) -> Unit,
) {
    AlertDialog(
        title = { Text(text = "选择主题") },
        onDismissRequest = onDismiss,
        text = {
            ThemeModeRadioGroup(
                currentThemeMode = currentThemeMode,
                onThemeModeChange = onThemeModeChange
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = "取消") }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeModeRadioGroup(
    currentThemeMode: Int,
    onThemeModeChange: (String) -> Unit,
) {
    val radioOptions = listOf("暗色", "亮色", "跟随系统")
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = text == radioOptions[currentThemeMode],
                        onClick = {
                            onThemeModeChange(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(16.dp)
            ) {
                RadioButton(
                    selected = text == radioOptions[currentThemeMode],
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}