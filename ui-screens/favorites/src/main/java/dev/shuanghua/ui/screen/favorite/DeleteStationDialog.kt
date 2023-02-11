package dev.shuanghua.ui.screen.favorite

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    text: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        text = { Text(text = text) },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(text = "删除")
            }
        }
    )
}
