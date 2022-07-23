package dev.shuanghua.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.shuanghua.weather.data.db.dao.*
import dev.shuanghua.weather.data.db.entity.*

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
        WeatherParam::class,

        FavoriteId::class,
        FavoriteCityWeather::class,
        Province::class,
        City::class
    ],
    version = 1,
    exportSchema = false
)
//@TypeConverters(TipInfoTypeConverters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun paramsDao(): ParamsDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        private const val APP_DATABASE_NAME = "WeatherApp.db"

        fun getInstance(
            context: Context
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(
            context: Context
        ) = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            APP_DATABASE_NAME
        ).build() // 👆 .addMigrations(MIGRATION_1_2)

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