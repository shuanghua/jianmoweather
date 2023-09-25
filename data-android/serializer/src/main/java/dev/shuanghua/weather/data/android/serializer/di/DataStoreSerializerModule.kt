package dev.shuanghua.weather.data.android.serializer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.android.serializer.DataStoreMoshiSerializer
import dev.shuanghua.weather.data.android.serializer.DataStoreSerialization


@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreSerializerModule {
	@Binds
	abstract fun bindsDataStoreMoshiSerializer(
		dataStoreMoshiSerializer: DataStoreMoshiSerializer
	): DataStoreSerialization
}