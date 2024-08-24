package dev.shuanghua.weather.data.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteCityWeatherEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationParamsEntity
import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

	//  ---------------------------------Station-------------------------------------------
	@Insert(onConflict = OnConflictStrategy.ABORT)
	suspend fun insertFavoriteStation(stationParam: FavoriteStationParamsEntity)


	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteStationsWeather(list: List<FavoriteStationWeatherEntity>)


	@Transaction
	@Query("SELECT * FROM favorite_station")
	fun observerFavoriteStations(): Flow<List<FavoriteStationParamsEntity>>


	@Query("DELETE FROM favorite_station WHERE stationName = :stationName")
	suspend fun deleteFavoriteStation(stationName: String)


	@Query("SELECT * FROM favorite_station WHERE stationName = :stationName")
	suspend fun getFavoriteStationByName(stationName: String): FavoriteStationParamsEntity


	// 观察站点天气信息
	@Transaction
	@Query("SELECT * FROM favorite_station_weather")
	fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeatherEntity>>


	@Query("DELETE FROM favorite_station_weather WHERE stationName = :stationName")
	suspend fun deleteFavoriteStationWeather(stationName: String)


	@Transaction
	suspend fun deleteFavoriteStationAndWeather(stationName: String) {
		deleteFavoriteStationWeather(stationName)
		deleteFavoriteStation(stationName)
	}

	//  ---------------------------------City---------------------------------------------

	// 插入城市信息-城市列表页面调用
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteCity(favoriteCityEntity: FavoriteCityEntity)


	// 观察城市信息
	@Transaction
	@Query("SELECT * FROM favorite_city")
	fun observerFavoriteCities(): Flow<List<FavoriteCityEntity>>


	// 插入城市天气信息
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteCitiesWeather(list: List<FavoriteCityWeatherEntity>)


	// 观察城市天气信息
	@Transaction
	@Query("SELECT * FROM favorite_city_weather")
	fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeatherEntity>>


	// 删除(收藏页面)
	@Query("DELETE FROM favorite_city WHERE cityId = :cityId")
	suspend fun deleteFavoriteCity(cityId: String)


	@Query("DELETE FROM favorite_city_weather WHERE cityId = :cityId")
	suspend fun deleteCityWeather(cityId: String)


	@Transaction
	suspend fun deleteCityAndWeather(cityId: String) {
		deleteFavoriteCity(cityId)
		deleteCityWeather(cityId)
	}


	// 清空所有城市天气信息
	@Query("DELETE FROM favorite_city_weather")
	suspend fun deleteAllCitiesWeather()

	// 清空所有站点天气信息
	@Query("DELETE FROM favorite_station_weather")
	suspend fun deleteAllStationsWeather()
}