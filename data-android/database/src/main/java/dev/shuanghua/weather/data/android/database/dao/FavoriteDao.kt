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

	//  ---------------------------------         Station       -------------------------------------
	//  --------------------------------- Station Request Param -------------------------------------
	@Insert(onConflict = OnConflictStrategy.ABORT)
	suspend fun insertFavoriteStationParams(stationParam: FavoriteStationParamsEntity)

	@Transaction
	@Query("SELECT * FROM favorite_station")
	fun observerFavoriteStations(): Flow<List<FavoriteStationParamsEntity>>

	@Query("DELETE FROM favorite_station WHERE stationName = :stationName")
	suspend fun deleteFavoriteStation(stationName: String)

	// 点击站点条目 需要传递参数到详情页面去请求
	@Query("SELECT * FROM favorite_station WHERE stationName = :stationName")
	suspend fun getFavoriteStationByName(stationName: String): FavoriteStationParamsEntity

	//  --------------------------------- Station Weather -------------------------------------------
	// 插入 站点天气
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteStationsWeather(list: List<FavoriteStationWeatherEntity>)

	// 观察站点天气信息
	@Transaction
	@Query("SELECT * FROM favorite_station_weather")
	fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeatherEntity>>

	// 删除 站点天气
	@Query("DELETE FROM favorite_station_weather WHERE stationName = :stationName")
	suspend fun deleteFavoriteStationWeather(stationName: String)

	// 清空 所有 站点天气信息 （当收藏 站点 被一个一个删除后， 也删除对应的天气数据）
	@Query("DELETE FROM favorite_station_weather")
	suspend fun deleteAllStationsWeather()

	// 删除 站点以及天气(外部调用)
	@Transaction
	suspend fun deleteFavoriteStationAndWeather(stationName: String) {
		deleteFavoriteStationWeather(stationName)
		deleteFavoriteStation(stationName)
	}




	//  ----------------------------------      City    -----------------------------------------
	//  ---------------------------------- City Favorite -----------------------------------------

	// 插入 收藏城市 [城市页面]
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteCity(favoriteCityEntity: FavoriteCityEntity)

	// 观察 城市信息 收藏页面
	@Transaction
	@Query("SELECT * FROM favorite_city")
	fun observerFavoriteCities(): Flow<List<FavoriteCityEntity>>

	// 删除 收藏城市 收藏页面
	@Query("DELETE FROM favorite_city WHERE cityId = :cityId")
	suspend fun deleteFavoriteCity(cityId: String)


	//  ---------------------------------- City Weather -----------------------------------------
	// 插入 城市天气信息 收藏页面
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavoriteCitiesWeather(list: List<FavoriteCityWeatherEntity>)

	// 观察 城市天气信息 收藏页面
	@Transaction
	@Query("SELECT * FROM favorite_city_weather")
	fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeatherEntity>>

	// 删除 城市天气信息
	@Query("DELETE FROM favorite_city_weather WHERE cityId = :cityId")
	suspend fun deleteCityWeather(cityId: String)

	// 清空 所有城市天气信息 （当收藏 城市 被一个一个删除后， 也删除对应的天气数据）
	@Query("DELETE FROM favorite_city_weather")
	suspend fun deleteAllCitiesWeather()

	// 删除 城市以及天气(外部调用)
	@Transaction
	suspend fun deleteCityAndWeather(cityId: String) {
		deleteFavoriteCity(cityId)
		deleteCityWeather(cityId)
	}
}