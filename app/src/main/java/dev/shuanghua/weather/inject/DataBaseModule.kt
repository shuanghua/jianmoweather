package dev.shuanghua.weather.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.db.AppDataBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


/**
 * 为整个项目范围内提供实例，比如非 Activity ,Fragment 等类。
 * 也可以为同级别的别的 Module 提供
 * 通过构造函数传入已实例化的实例
 * 该负责存储已实例化的实例
 * @Provides 将该实例向外提供
 */
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Singleton
    @Provides
    fun provideParamsDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).paramsDao()

    @Singleton
    @Provides
    fun provideWeatherDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).weatherDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).favoriteDao()

    @Singleton
    @Provides
    fun provideProvinceDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).provinceDao()

    @Singleton
    @Provides
    fun provideCityDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).cityDao()

    @Singleton
    @Provides
    fun provideDistrictDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).districtDao()

    @Singleton
    @Provides
    fun provideStationDao(
        @ApplicationContext appContext: Context
    ) = AppDataBase.getInstance(
        appContext.applicationContext
    ).stationDao()

}