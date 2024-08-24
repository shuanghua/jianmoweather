package dev.shuanghua.weather.data.android.network.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializerModule = module {
	single {
		val format = Json {
			ignoreUnknownKeys = true    // 忽略服务器下发的多余字段
			explicitNulls = false    // 不序列化 null 值
		}
	}
}