package dev.shuanghua.weather.data.android.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.database.AppDataBase
import javax.inject.Singleton


/**
 * 为整个项目范围内提供实例，比如非 Activity ,Fragment 等类。
 * 也可以为同级别的别的 Module 提供
 * 通过构造函数传入已实例化的实例
 * 该负责存储已实例化的实例
 * @Provides 将该实例向外提供
 */
@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Singleton
    @Provides
    fun provideWeatherDao(
        dataBase: AppDataBase
    ) = dataBase.weatherDao()


    @Singleton
    @Provides
    fun provideFavoriteDao(
        dataBase: AppDataBase
    ) = dataBase.favoriteDao()


    @Singleton
    @Provides
    fun provideProvinceDao(
        dataBase: AppDataBase
    ) = dataBase.provinceDao()


    @Singleton
    @Provides
    fun provideCityDao(
        dataBase: AppDataBase
    ) = dataBase.cityDao()


    @Singleton
    @Provides
    fun provideDistrictDao(
        dataBase: AppDataBase
    ) = dataBase.districtDao()


    @Singleton
    @Provides
    fun provideStationDao(
        dataBase: AppDataBase
    ) = dataBase.stationDao()

}