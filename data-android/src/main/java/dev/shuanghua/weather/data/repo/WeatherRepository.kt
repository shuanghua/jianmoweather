package dev.shuanghua.weather.data.repo

import dev.shuanghua.weather.data.db.pojo.PackingWeather
import dev.shuanghua.weather.data.model.WeatherResource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun updateWeather(params: String)
    fun getWeather(): Flow<WeatherResource>   //看ViewModel需要什么数据
}