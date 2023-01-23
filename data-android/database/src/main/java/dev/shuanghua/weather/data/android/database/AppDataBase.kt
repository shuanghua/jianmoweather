package dev.shuanghua.weather.data.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shuanghua.weather.data.android.database.dao.CityDao
import dev.shuanghua.weather.data.android.database.dao.DistrictDao
import dev.shuanghua.weather.data.android.database.dao.FavoriteDao
import dev.shuanghua.weather.data.android.database.dao.ProvinceDao
import dev.shuanghua.weather.data.android.database.dao.StationDao
import dev.shuanghua.weather.data.android.database.dao.WeatherDao
import dev.shuanghua.weather.data.android.database.entity.AlarmIconEntity
import dev.shuanghua.weather.data.android.database.entity.CityEntity
import dev.shuanghua.weather.data.android.database.entity.ConditionEntity
import dev.shuanghua.weather.data.android.database.entity.DistrictEntity
import dev.shuanghua.weather.data.android.database.entity.ExponentEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityIdEntity
import dev.shuanghua.weather.data.android.database.entity.HalfHourEntity
import dev.shuanghua.weather.data.android.database.entity.OneDayEntity
import dev.shuanghua.weather.data.android.database.entity.OneHourEntity
import dev.shuanghua.weather.data.android.database.entity.ProvinceEntity
import dev.shuanghua.weather.data.android.database.entity.SelectedStationEntity
import dev.shuanghua.weather.data.android.database.entity.StationEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherParamsEntity

@Database(
    entities = [
        WeatherEntity::class,
        AlarmIconEntity::class,
        ConditionEntity::class,
        ExponentEntity::class,
        OneHourEntity::class,
        OneDayEntity::class,
        HalfHourEntity::class,

        FavoriteCityIdEntity::class,
        WeatherParamsEntity::class,

        ProvinceEntity::class,
        CityEntity::class,

        DistrictEntity::class,
        StationEntity::class,
        SelectedStationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun cityDao(): CityDao
    abstract fun districtDao(): DistrictDao
    abstract fun stationDao(): StationDao
}

//private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        //database.execSQL("ALTER TABLE SZWeather ADD COLUMN weather_desc TEXT")
//        database.execSQL("CREATE TABLE Alarm(_id INTEGER Not Null,icon TEXT Not Null,name TEXT Not Null,alarmCityId TEXT Not Null,PRIMARY KEY(_id), FOREIGN KEY(alarmCityId) REFERENCES SZWeather(cityId) ON DELETE CASCADE)")
//        database.execSQL("CREATE INDEX index_Alarm_alarmCityId ON Alarm(alarmCityId)")
//    }
//}