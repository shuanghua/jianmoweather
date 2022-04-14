package jianmoweather.data.repo.location

import jianmoweather.data.db.dao.ParamsDao
import jianmoweather.data.db.entity.Params
import jianmoweather.data.repo.city.CityRemoteDataSource
import jianmoweather.data.repo.location.LocationRepository.Location
import kotlinx.coroutines.flow.Flow

/**
 * 定位 + 查询城市id
 */
class ParamsRepository(
	private val paramsDao: ParamsDao,
	private val cityRemoteDataSource: CityRemoteDataSource
) {
	private var _currentParams: Params = Params(cityId = "")

	fun observerRequestParams(isLocation: Int): Flow<Params?> {
		return paramsDao.observerRequestParam(isLocation)
	}


	/**
	 * 获取定位 AMapLocation
	 * 获取定位城市id String
	 * 创建请求参数 Params
	 * 返回 Params
	 */
	suspend fun getRequestParams(location: Location?): Params {
		return if(location != null) {
			createNewRequestParams(location)
		} else {
			paramsDao.findWeatherRequestParam(1) ?: defaultParams
		}
	}

	private suspend fun createNewRequestParams(location: Location): Params {
		val id = if(_currentParams.cityName != location.cityName) { // 如果和上次城市一样   // 使用上一次的城市 id
			getCurrentCityId(location)
		} else {          // 来到一个新的城市
			_currentParams.cityId
		}
		updateCurrentParams(location)
		val newParams = _currentParams.copy(cityId = id)
		_currentParams = newParams
		paramsDao.insertWeatherParam(newParams)
		return newParams
	}

	/**
	 *  获取当前城市的 id
	 */
	private suspend fun getCurrentCityId(location: Location): String {
		return cityRemoteDataSource.fetchCityId(
			location.cityName,
			location.latitude,
			location.longitude,
			location.district
		)
	}

	private fun updateCurrentParams(location: Location) {
		_currentParams = Params(
			cityName = location.cityName,
			district = location.district,
			latitude = location.latitude,
			longitude = location.longitude
		)
	}


	companion object {

		// 只有首次安装APP 并且 定位失败 才会使用默认的深圳参数
		private val defaultParams = Params()

		@Volatile
		private var INSTANCE: ParamsRepository? = null
		fun getInstance(
			paramsDao: ParamsDao,
			cityRemoteDataSource: CityRemoteDataSource

		): ParamsRepository {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: ParamsRepository(
					paramsDao,
					cityRemoteDataSource
				).also {
					INSTANCE = it
				}
			}
		}
	}
}



