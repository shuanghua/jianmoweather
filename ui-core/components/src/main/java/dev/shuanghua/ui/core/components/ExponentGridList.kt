package dev.shuanghua.ui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shuanghua.weather.data.android.model.Exponent

@Composable
fun ExponentItems(
    exponents: List<Exponent>,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        modifier = modifier
            .padding(vertical = 16.dp)
            .height(200.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(exponents) { exponent ->
            ExponentItem(
                title = exponent.title,
                levelDesc = exponent.levelDesc,
            )
        }
    }
}