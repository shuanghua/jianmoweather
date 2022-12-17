package dev.shuanghua.ui.web

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
) : ViewModel() {

    private val url: String =
        checkNotNull(savedStateHandle[WebDestination.urlArg])
}