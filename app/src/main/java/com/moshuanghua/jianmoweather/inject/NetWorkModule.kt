package com.moshuanghua.jianmoweather.inject

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.repo.city.CityRemoteDataSource
import jianmoweather.data.repo.weather.WeatherRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    // -----------------------------------DataSource ↙------------------------------------------//
    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(service: ShenZhenService): WeatherRemoteDataSource {
        return WeatherRemoteDataSource.getInstance(service)
    }

    @Singleton
    @Provides
    fun provideCityRemoteDataSource(service: ShenZhenService): CityRemoteDataSource {
        return CityRemoteDataSource(service)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY

        //val loggingInterceptor = HttpLoggingInterceptor()
        //loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(6000L, TimeUnit.MILLISECONDS)
            .writeTimeout(6000L, TimeUnit.MILLISECONDS)
            .readTimeout(6000L, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideShenZhenService(okHttpClient: OkHttpClient): ShenZhenService {
        val moshi = Moshi.Builder()
            .build()
        return Retrofit.Builder()
            .baseUrl(ShenZhenService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
            .create(ShenZhenService::class.java)
    }
}