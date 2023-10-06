package dev.shuanghua.ui.screen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
	onBackClick: () -> Unit,
	viewModel: SettingsViewModel = hiltViewModel(),
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	val uiState: SettingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

	Scaffold(
		topBar = {
			SettingTopBar(
				scrollBehavior = scrollBehavior,
				onBackClick = onBackClick
			)
		}
	) { paddingValues ->
		LazyColumn(
			contentPadding = PaddingValues(top = paddingValues.calculateTopPadding() + 16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
		) {

			item {
				val openThemeChangeDialog = remember { mutableStateOf(false) }
				when (uiState) {
					is SettingsUiState.Loading -> {}
					is SettingsUiState.Error -> {
						Timber.e((uiState as SettingsUiState.Error).errorMessage)
					}

					is SettingsUiState.Success -> {
						SettingItem(
							title = "主题选择",
							description = "主题选择",
							icon = Icons.Outlined.Palette,
							onClick = { openThemeChangeDialog.value = true }
						)
						if (openThemeChangeDialog.value) {
							ChangeThemeDialog(
								themeSettings = (uiState as SettingsUiState.Success).themeSettings,
								onDismiss = { openThemeChangeDialog.value = false },
								onChangeThemeConfig = { viewModel.updateTheme(it) }
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun SettingItem(
	title: String,
	description: String,
	icon: ImageVector?,
	onClick: () -> Unit,
) {
	Surface(
		modifier = Modifier.clickable { onClick() }
	) {
		Row(
			modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 20.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			icon?.let {
				Icon(
					imageVector = icon,
					contentDescription = null,
					modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp),
					tint = MaterialTheme.colorScheme.secondary
				)
			}
			Column(
				modifier = Modifier
                    .weight(1f)
                    .padding(start = if (icon == null) 12.dp else 0.dp)
			) {
				Text(
					text = title,
					maxLines = 1,
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = description,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					maxLines = 1,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopBar(
	scrollBehavior: TopAppBarScrollBehavior,
	onBackClick: () -> Unit,
) {
	TopAppBar(
		scrollBehavior = scrollBehavior,
		title = { Text(text = "设置") },
		navigationIcon = {
			IconButton(onClick = { onBackClick() }) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "返回"
				)
			}
		}
	)
}