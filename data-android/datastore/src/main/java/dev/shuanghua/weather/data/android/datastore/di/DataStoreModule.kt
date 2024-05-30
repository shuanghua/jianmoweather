package dev.shuanghua.weather.data.android.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSourceImpl
import dev.shuanghua.weather.data.android.datastore.AppPreferences
import dev.shuanghua.weather.data.android.datastore.serialization.AppDataSerializer
import org.koin.dsl.module

val dataStoreModule = module {
	single<AppDataStoreDataSource> { AppDataStoreDataSourceImpl(get()) }
	single<DataStore<AppPreferences>> {
		DataStoreFactory.create(AppDataSerializer) {
			get<Context>().dataStoreFile("datastore.pb")
		}
	}
}
