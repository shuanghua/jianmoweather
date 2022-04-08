package jianmoweather.data.repo.location

import com.amap.api.location.AMapLocation
import com.moshuanghua.jianmoweather.shared.Result
import jianmoweather.data.db.dao.ParamsDao
import jianmoweather.data.db.entity.Params
import jianmoweather.data.repo.city.CityRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ParamsRepository(
    private val paramsDao: ParamsDao,
    private val locationRemoteDataSource: LocationDataSource,
    private val cityRemoteDataSource: CityRemoteDataSource
) {

    private var cityName = ""
    private var cityId = ""

    fun observerRequestParams(isLocation: Int): Flow<Params?> {
        return paramsDao.observerRequestParam(isLocation)
    }


    /**
     * 获取定位 AMapLocation
     * 获取定位城市id String
     * 创建请求参数 Params
     * 返回 Params
     */
    suspend fun getRequestParams(): Params {
        return when(val result = locationRemoteDataSource.getOnceLocation()) {
            is Result.Success -> {
                val newParams = createNewRequestParams(result.data)
                paramsDao.insertWeatherParam(newParams)
                newParams
            }
            is Result.Error -> {
                paramsDao.findWeatherRequestParam(0) ?: defaultParams
            }
        }
    }

    private suspend fun createNewRequestParams(location: AMapLocation): Params {
        // kotlin 引用比较 "==="  等同 java "=="
        // kotlin 的 equals 和 kotlin 的 "==" 是一样的，属于结构比较
        // kotlin 的 equals 通常用于数据类重写，一旦重写了，就意味这改变 == 的判断逻辑
        // 只要内容相等，不管地址是否相等
        return if(cityName == location.city) {
            Params(
                cityId = cityId, // 使用上一次的城市 id
                cityName = location.city,
                district = location.district,
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString()
            )
        } else {// 来到一个新的城市
            cityName = location.city
            cityId = cityRemoteDataSource.fetchCityId(
                location.city,
                location.latitude.toString(),
                location.longitude.toString(),
                location.district
            )
            Params(
                cityId = cityId,
                cityName = location.city,
                district = location.district,
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString(),
            )
        }
    }


    companion object {

        // 只有首次安装APP 并且 定位失败 才会使用默认的深圳参数
        private val defaultParams = Params()

        @Volatile
        private var INSTANCE: ParamsRepository? = null
        fun getInstance(
            remoteDataSource: LocationDataSource,
            cityRemoteDataSource: CityRemoteDataSource,
            paramsDao: ParamsDao

        ): ParamsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ParamsRepository(
                    paramsDao,
                    remoteDataSource,
                    cityRemoteDataSource
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}



