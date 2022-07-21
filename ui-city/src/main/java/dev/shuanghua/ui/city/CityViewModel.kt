package dev.shuanghua.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.repo.city.CityRepository
import javax.inject.Inject

@HiltViewModel
class CityViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
    private val cityRepository:CityRepository
) : ViewModel() {

    private val provinceId: String = checkNotNull(savedStateHandle[CityScreenDestination.provinceIdArg])
    private val provinceName: String = checkNotNull(savedStateHandle[CityScreenDestination.provinceNameArg])

    fun getCityList(provinceId:String) {

    }

    fun addCityIdToFavorite(cityId:String){

    }
}