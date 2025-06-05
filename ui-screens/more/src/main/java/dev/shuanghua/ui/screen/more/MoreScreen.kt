package dev.shuanghua.ui.screen.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreScreen(
	navigateToWeb: (String) -> Unit,
	navigateToSettings: () -> Unit,
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			MoreScreenTopBar(
				scrollBehavior = scrollBehavior,
				navigateToSettings = navigateToSettings
			)
		}
	) { innerPadding ->
		LazyColumn(
			contentPadding = PaddingValues(top = innerPadding.calculateTopPadding() + 16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.nestedScroll(scrollBehavior.nestedScrollConnection)
				.fillMaxSize()
				.padding(horizontal = 16.dp)
		) {

			item {
				MoreItem(
					title = "全球气象",
					subTitle = "earth.nullschool",
					url = "https://earth.nullschool.net/zh-cn",
					navigateToWeb = navigateToWeb
				)
			}

			item {
				MoreItem(
					title = "台风路径",
					subTitle = "深圳APP",
					url = "https://szqxapp1.121.com.cn/phone/app/webPage/typhoon/typhoon.html",
					navigateToWeb = navigateToWeb
				)
			}

			item {
				MoreItem(
					title = "台风路径",
					subTitle = "深圳台风网",
					url = "https://tf.121.com.cn/wap.htm",
					navigateToWeb = navigateToWeb
				)
			}

			item {
				MoreItem(
					title = "台风路径",
					subTitle = "iStrongCloud",
					url = "https://tf.istrongcloud.com/",
					navigateToWeb = navigateToWeb
				)
			}
		}
	}
}
//MoreItem(navigateToWeb = navigateToWeb)

@Composable
fun MoreItem(
	title: String,
	subTitle: String,
	url: String,
	navigateToWeb: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(160.dp)
			.clip(shape = RoundedCornerShape(16.dp))
			.background(
				color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
			)
			.clickable { navigateToWeb(url) }
	) {
		Column(
			modifier = modifier
				.align(Alignment.CenterStart)
				.padding(start = 16.dp)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.headlineSmall,
				textAlign = TextAlign.Start
			)

			Text(
				text = subTitle,
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Start
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreenTopBar(
	navigateToSettings: () -> Unit,
	scrollBehavior: TopAppBarScrollBehavior,
) {
	TopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = "更多") },
		actions = {
			IconButton(onClick = navigateToSettings) {
				Icon(
					imageVector = Icons.Filled.Settings,
					contentDescription = "设置"
				)
			}
			// 添加更多按钮
		}
	)
}