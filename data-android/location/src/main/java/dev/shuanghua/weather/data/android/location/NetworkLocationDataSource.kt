package dev.shuanghua.weather.data.android.location

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import dev.shuanghua.weather.data.android.location.model.NetworkLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume


sealed class Result<out R> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val errorMessage: String) : Result<Nothing>()
}


private fun AMapLocation.asExternalModel() = NetworkLocation(
    cityName = city,
    latitude = latitude.toString(),
    longitude = longitude.toString(),
    district = district,
    address = address,
)


private fun AMapLocation.asLocationResult(): Result<NetworkLocation> = when {
    errorCode != 0 -> Result.Error("$errorCode $locationDetail")

    city.isNullOrEmpty() -> Result.Error("定位城市为空")

    city == "Mountain View" -> Result.Error("暂时不支持该城市☞ $city")

    else -> Result.Success(asExternalModel())
}


class NetworkLocationDataSource @Inject constructor(
    private val client: AMapLocationClient
) {
    suspend fun getNetworkLocation(): Result<NetworkLocation> { // 一次性监听
        return suspendCancellableCoroutine { cont ->
            val callback = AMapLocationListener { location ->
                location ?: return@AMapLocationListener
                cont.resume(location.asLocationResult())
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