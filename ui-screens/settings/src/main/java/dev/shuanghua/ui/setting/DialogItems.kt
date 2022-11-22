package dev.shuanghua.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun ThemeChangeDialog(
    onDismissRequest: () -> Unit, // dialog关闭
    onConfirm: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "取消")
            }
        },
        title = { Text(text = "主题选择") },
        text = {
            val selected = remember {
                mutableStateOf(true)
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                SingleChoiceItem(text = "白色", selected = true, onClick = {
                    selected.value = true
                })
                SingleChoiceItem(text = "暗色", selected = false, onClick = {

                })
                SingleChoiceItem(text = "跟随系统", selected = false, onClick = {

                })
            }
        }
    )
}