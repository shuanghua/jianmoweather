package dev.shuanghua.weather.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

//companion object operator invoke
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            dataStoreRepository.setThemeMode(2)
        }
    }

    fun getThemeModel(): Flow<Int> {
        return dataStoreRepository.themeMode
    }
}