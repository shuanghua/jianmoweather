package dev.shuanghua.weather.data.android.network.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.network.api.ShenZhenWeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 提供第三方库的调用对象
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient
            .Builder()
            .connectTimeout(6000L, TimeUnit.MILLISECONDS)
            .writeTimeout(6000L, TimeUnit.MILLISECONDS)
            .readTimeout(6000L, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun providesWeatherApi(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): ShenZhenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(ShenZhenWeatherApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(ShenZhenWeatherApi::class.java)
    }
}