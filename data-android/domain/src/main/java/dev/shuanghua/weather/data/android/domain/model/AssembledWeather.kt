package dev.shuanghua.weather.data.android.domain.model

import dev.shuanghua.weather.data.android.model.Weather

/**
 * ui model
 * 组装各个model 方便供 ui 使用
 * 如果不需要组装，则直接使用 model 模块的模型即可
 */
data class AssembledWeather(val weather: Weather)