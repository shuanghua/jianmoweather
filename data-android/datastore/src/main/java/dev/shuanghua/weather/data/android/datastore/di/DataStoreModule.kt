package dev.shuanghua.weather.data.android.datastore.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSource
import dev.shuanghua.weather.data.android.datastore.AppDataStoreDataSourceImpl
import dev.shuanghua.weather.data.android.datastore.model.DataStoreModel
import dev.shuanghua.weather.data.android.datastore.serialization.AppDataSerializer
import dev.shuanghua.weather.data.android.datastore.serialization.DataStoreSerialization
import dev.shuanghua.weather.data.android.datastore.serialization.DataStoreSerializer
import org.koin.dsl.module


val dataStoreModule = module {
	single<JsonAdapter<DataStoreModel>> {
		val moshi: Moshi = Moshi.Builder().build()
		moshi.adapter(DataStoreModel::class.java)
	}
	single<DataStoreSerialization> { DataStoreSerializer(get()) }
	single<Serializer<DataStoreModel>> { AppDataSerializer(get()) }
	single<DataStore<DataStoreModel>> {
		val dsg: DataStore<DataStoreModel> = DataStoreFactory.create(
			serializer = get(),
		) {
			get<Application>().dataStoreFile("datastore.pb")
		}
		dsg
	}
	single<AppDataStoreDataSource> { AppDataStoreDataSourceImpl(get()) }
}
