package dev.shuanghua.weather.data.android.network.di

import androidx.core.os.trace
import com.squareup.moshi.Moshi
import dev.shuanghua.weather.data.android.network.NetworkDataSource
import dev.shuanghua.weather.data.android.network.NetworkDataSourceImpl
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.api.ShenZhenApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val networkModule = module {
	single<Moshi> { Moshi.Builder().build() }
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
		Retrofit.Builder()
			.baseUrl(Api2.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create(get()))
			.client(get())
			.build()
			.create(ShenZhenApiService::class.java)
	}
	// 其它 api 源

	single<NetworkDataSource> { NetworkDataSourceImpl(get()) }
}