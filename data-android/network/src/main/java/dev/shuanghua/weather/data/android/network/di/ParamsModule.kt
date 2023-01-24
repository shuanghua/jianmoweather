package dev.shuanghua.weather.data.android.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.network.MoshiSerialization
import dev.shuanghua.weather.data.android.network.SerializationFactory

/**
 * 向外提供 SerializationFactory
 * SerializationFactory 是接口，没有构造函数
 * 需要使用 interface module - T.binds()
 */
@Module
@InstallIn(SingletonComponent::class)
interface ParamsModule {
    @Binds
    fun MoshiSerialization.binds(): SerializationFactory
}