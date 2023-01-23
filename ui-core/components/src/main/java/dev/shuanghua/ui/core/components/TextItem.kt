package dev.shuanghua.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JmoTextItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme
                .typography
                .labelMedium
                .copy(fontSize = 20.sp)
        )
    }
}

@Preview
@Composable
internal fun PreViewTextItem(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.fillMaxSize()) {
        repeat(20) {
            item {
                JmoTextItem(
                    text = "北京",
                    onClick = {}
                )
            }
        }
    }
}