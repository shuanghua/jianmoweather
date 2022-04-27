package dev.shuanghua.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.shuanghua.weather.data.db.dao.CityDao
import dev.shuanghua.weather.data.db.dao.ParamsDao
import dev.shuanghua.weather.data.db.dao.WeatherDao
import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.data.network.MainWeatherParam
import dev.shuanghua.weather.data.network.OuterParam


@Database(
    entities = [
        Temperature::class,
        Alarm::class,
        Condition::class,
        Exponent::class,
        OneHour::class,
        OneDay::class,
        HalfHour::class,
        OuterParam::class,
        MainWeatherParam::class,
        Province::class,
        City::class
    ],
    version = 1,
    exportSchema = false
)
//@TypeConverters(TipInfoTypeConverters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
    abstract fun paramsDao(): ParamsDao

    companion object {
        @Volatile
        private var INSTANCE: dev.shuanghua.weather.data.db.AppDataBase? = null
        private const val APP_DATABASE_NAME = "WeatherApp.db"

        fun getInstance(context: Context): dev.shuanghua.weather.data.db.AppDataBase = dev.shuanghua.weather.data.db.AppDataBase.Companion.INSTANCE
            ?: synchronized(this) {
                dev.shuanghua.weather.data.db.AppDataBase.Companion.INSTANCE
                    ?: dev.shuanghua.weather.data.db.AppDataBase.Companion.buildDatabase(
                        context
                    ).also { dev.shuanghua.weather.data.db.AppDataBase.Companion.INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context,
                dev.shuanghua.weather.data.db.AppDataBase::class.java,
                dev.shuanghua.weather.data.db.AppDataBase.Companion.APP_DATABASE_NAME
            )
            //.addMigrations(MIGRATION_1_2)
            .build()

        //数据库升级
//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                //database.execSQL("ALTER TABLE SZWeather ADD COLUMN weather_desc TEXT")
//                database.execSQL("CREATE TABLE Alarm(_id INTEGER Not Null,icon TEXT Not Null,name TEXT Not Null,alarmCityId TEXT Not Null,PRIMARY KEY(_id), FOREIGN KEY(alarmCityId) REFERENCES SZWeather(cityId) ON DELETE CASCADE)")
//                database.execSQL("CREATE INDEX index_Alarm_alarmCityId ON Alarm(alarmCityId)")
//            }
//        }

//        private fun buildDatabase(context: Context) = Room
//                .databaseBuilder(
//                        context.applicationContext,
//                        AppDataBase::class.java, APP_DATABASE_NAME
//                )
//                .addMigrations(MIGRATION_1_2)
//                .build()
    }
}