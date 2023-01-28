package dev.shuanghua.weather.data.android.serializer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.serializer.NetworkParamsMoshiSerializer
import dev.shuanghua.weather.data.android.serializer.NetworkParamsSerialization

/**
 * 用于向外提供 NetworkParamsSerialization
 * SerializationFactory 是接口，没有构造函数
 * 需要使用 interface module - T.binds()
 */
@Module
@InstallIn(SingletonComponent::class)
interface NetworkParamsSerializerModule {
    @Binds
    fun NetworkParamsMoshiSerializer.binds(): NetworkParamsSerialization
}