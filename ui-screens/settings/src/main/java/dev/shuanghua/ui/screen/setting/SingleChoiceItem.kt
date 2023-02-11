package dev.shuanghua.ui.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SingleChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                enabled = true,
                onClick = onClick,
                role = Role.RadioButton
            )
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Text(
            modifier = modifier.padding(start = 16.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}