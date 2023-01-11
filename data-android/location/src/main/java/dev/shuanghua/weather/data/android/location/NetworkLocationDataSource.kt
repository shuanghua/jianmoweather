package dev.shuanghua.weather.data.android.location

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import dev.shuanghua.weather.data.android.location.model.GaoDeLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume


sealed class LocationResult<out R> {
    data class Success<T>(val data: T) : LocationResult<T>()
    data class Error(val errorMessage: String) : LocationResult<Nothing>()
}


private fun AMapLocation.asExternalModel(): GaoDeLocation = GaoDeLocation(
    cityName = city,
    latitude = latitude.toString(),
    longitude = longitude.toString(),
    district = district,
    address = address,
)


private fun AMapLocation.status(): LocationResult<GaoDeLocation> = when {
    errorCode != 0 -> LocationResult.Error("$errorCode $locationDetail")
    city.isNullOrEmpty() -> LocationResult.Error("定位城市为空")
    city == "Mountain View" -> LocationResult.Error("暂时不支持该城市☞ $city")
    else -> LocationResult.Success(asExternalModel())
}


class NetworkLocationDataSource @Inject constructor(private val client: AMapLocationClient) {
    suspend fun getOnceLocationResult(): LocationResult<GaoDeLocation> { // 一次性监听
        return suspendCancellableCoroutine { cont ->
            val callback = AMapLocationListener { location ->
                location ?: return@AMapLocationListener
                cont.resume(location.status())
            }
            client.setLocationListener(callback)
            client.startLocation()
            cont.invokeOnCancellation { client.unRegisterLocationListener(callback) }
        }
    }


    /**
     * 一对多定位监听
     */
//	fun observerLocation(): Flow<AMapLocation> = callbackFlow {
//		val callback = AMapLocationListener { location ->
//			when(val result = location.status()) {
//				is LocationSuccess -> {
//					trySendBlocking(location)
//				}
//				is LocationError -> cancel(CancellationException(result.throwable.message))
//			}
//		}
//		client.setLocationListener(callback)
//		awaitClose { client.unRegisterLocationListener(callback) } // 当订阅者停止监听，利用挂起函数 "awaitClose" 来解除订阅
//	}


    /**
     * 传入 Lambda
     * 一对多定位监听
     */
//	suspend fun observeLocation(observer: suspend (Result<AMapLocation>) -> Unit) = coroutineScope {
//		val done = CompletableDeferred<Unit>()
//		val callback = object : AMapLocationListener {
//			val mutex = Mutex() //互斥锁，先到先获得锁
//			var observeJob: Job? = null
//			override fun onLocationChanged(location: AMapLocation) {
//				observeJob?.cancel()
//				observeJob = launch {
//					mutex.withLock {
//						//observer(location.status())
//					}
//				}
//			}
//		}
//		try {
//			client.setLocationListener(callback)
//			delay(3000L)
//			client.startLocation()//如果设置的定位时间间隔较长，请先添加监听再start, 不然有可能错过第一次回调
//			done.await()//确保每一个CallBack都能完成回调
//		} finally {
//			client.unRegisterLocationListener(callback)
//		}
//	}
}