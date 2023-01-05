package dev.shuanghua.ui.more

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.datastore.settings.SettingsDataSource
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val dataStoreRepository: SettingsDataSource,
) : ViewModel() {
}