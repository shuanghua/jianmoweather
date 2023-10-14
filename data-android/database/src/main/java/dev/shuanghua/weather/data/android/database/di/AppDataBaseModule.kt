package dev.shuanghua.weather.data.android.database.di

import androidx.room.Room
import dev.shuanghua.weather.data.android.database.AppDataBase
import org.koin.dsl.module

val dataBaseModule = module {
	single {
		Room.databaseBuilder(
			get(),
			AppDataBase::class.java,
			"app-database"
		).build()
	}

	single { get<AppDataBase>().weatherDao() }

	single { get<AppDataBase>().favoriteDao() }

	single { get<AppDataBase>().provinceDao() }

	single { get<AppDataBase>().cityDao() }

	single { get<AppDataBase>().districtDao() }

	single { get<AppDataBase>().stationDao() }
}