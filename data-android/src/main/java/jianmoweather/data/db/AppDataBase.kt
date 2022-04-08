package jianmoweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cjianmoweather.data.db.entity.Favorite
import jianmoweather.data.db.dao.CityDao
import jianmoweather.data.db.dao.ParamsDao
import jianmoweather.data.db.dao.WeatherDao
import jianmoweather.data.db.entity.*


@Database(
    entities = [
        WeatherScreenEntity::class,
        Alarm::class,
        OtherItem::class,
        HealthExponent::class,
        OneHour::class,
        OneDay::class,
        HalfHour::class,

        Params::class,

        Province::class,
        City::class,
        Favorite::class
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
        private var INSTANCE: AppDataBase? = null
        private const val APP_DATABASE_NAME = "WeatherApp.db"

        fun getInstance(context: Context): AppDataBase = INSTANCE
            ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    ).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context,
                AppDataBase::class.java,
                APP_DATABASE_NAME
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