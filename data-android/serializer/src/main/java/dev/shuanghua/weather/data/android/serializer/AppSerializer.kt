package dev.shuanghua.weather.data.android.network

import kotlinx.serialization.json.Json

object AppSerializer {
	val defaultJson = Json {
		ignoreUnknownKeys = true    // 忽略服务器下发的多余字段
		explicitNulls = false    // 不序列化 null 值
	}
}

