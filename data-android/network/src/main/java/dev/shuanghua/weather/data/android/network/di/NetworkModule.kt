package dev.shuanghua.weather.data.android.network.di

import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.NetworkDataSourceImpl
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.api.ShenZhenApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val networkModule = module {
	single<OkHttpClient> {
		val logging = HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }
		logging.level = HttpLoggingInterceptor.Level.BODY
		OkHttpClient.Builder()
			.connectTimeout(6000L, TimeUnit.MILLISECONDS)
			.writeTimeout(6000L, TimeUnit.MILLISECONDS)
			.readTimeout(6000L, TimeUnit.MILLISECONDS)
			.addInterceptor(logging)
			.build()
	}
	single<ShenZhenApiService> {
		val contentType = "application/json; charset=utf-8".toMediaType()
		// 服务器会下发很多的字段, 我们只需要其中部分的字段, 所以忽略不使用的字段
		// 使用的字段可能带有 null 值, 有时不会, 所以我们需要用 explicitNulls = false 来禁止序列/反序列化 null 值
		val format = Json {
			ignoreUnknownKeys = true    // 忽略服务器下发的多余字段
			explicitNulls = false    // 不序列化 null 值
		}
		Retrofit.Builder()
			.baseUrl(Api2.BASE_URL)
			.addConverterFactory(format.asConverterFactory(contentType))
			.client(get())
			.build()
			.create(ShenZhenApiService::class.java)
	}

	single<NetworkDataSource> {
		NetworkDataSourceImpl(androidContext(), get())
	}
}