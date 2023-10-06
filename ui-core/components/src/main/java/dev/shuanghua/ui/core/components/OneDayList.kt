package dev.shuanghua.ui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shuanghua.weather.data.android.model.OneDay

@Composable
fun OneDayList(
	oneDays: List<OneDay>,
	modifier: Modifier = Modifier
) {
	LazyRow(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		contentPadding = PaddingValues(horizontal = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(items = oneDays, key = { it.id }) {
			DayItem(
				topText = it.week,
				centerIconUrl = it.iconUrl,
				bottomText = it.t,
				dialogText = it.desc
			)
		}
	}
}