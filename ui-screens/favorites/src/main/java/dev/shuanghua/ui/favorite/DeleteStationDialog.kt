package dev.shuanghua.ui.favorite

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteStationDialog(
    onDeleteStation: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        text = { Text(text = "确认删除吗？删除后将不可恢复！") },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteStation) {
                Text(text = "删除")
            }
        }
    )
}
