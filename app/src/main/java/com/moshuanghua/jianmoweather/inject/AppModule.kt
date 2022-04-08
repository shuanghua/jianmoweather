package com.moshuanghua.jianmoweather.inject

import android.content.Context
import android.content.SharedPreferences
import com.moshuanghua.jianmoweather.shared.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jianmoweather.data.db.AppDataBase
import jianmoweather.data.db.dao.CityDao
import jianmoweather.data.db.dao.ParamsDao
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.network.ShenZhenService
import jianmoweather.data.repo.city.CityRemoteDataSource
import jianmoweather.data.repo.city.CityRepository
import jianmoweather.data.repo.favorite.FavoriteLocalDataSource
import jianmoweather.data.repo.favorite.FavoriteRepository
import jianmoweather.data.repo.province.ProvinceLocalDataSource
import jianmoweather.data.repo.province.ProvinceRemoteDataSource
import jianmoweather.data.repo.province.ProvinceRepository
import jianmoweather.data.repo.station.StationRepository
import jianmoweather.data.repo.weather.WeatherRemoteDataSource
import jianmoweather.data.repo.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
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
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    // -----------------------------------Room Dao ↙--------------------------------------------//
    @Singleton
    @Provides
    fun provideWeatherDao(@ApplicationContext appContext: Context): WeatherDao {
        return AppDataBase.getInstance(appContext.applicationContext).weatherDao()
    }

    @Singleton
    @Provides
    fun provideCityDao(@ApplicationContext appContext: Context): CityDao {
        return AppDataBase.getInstance(appContext.applicationContext).cityDao()
    }

    @Singleton
    @Provides
    fun provideParamsDao(@ApplicationContext appContext: Context): ParamsDao {
        return AppDataBase.getInstance(appContext.applicationContext).paramsDao()
    }
}