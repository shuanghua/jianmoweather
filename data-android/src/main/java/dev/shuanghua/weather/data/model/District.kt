package dev.shuanghua.weather.data.model

data class District(
    val name: String,
    val list: MutableList<Station>
)