package dev.shuanghua.weather.data.repo

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


sealed class LocationResult<out R>
data class LocationSuccess<T>(val data: T) : LocationResult<T>()
data class LocationError(val throwable: Throwable) : LocationResult<Nothing>()

private fun AMapLocation.status(): LocationResult<AMapLocation> = when {

	this.errorCode != 0 -> {
		LocationError(Exception("定位错误: ${this.errorCode} (${this.locationDetail})"))
	}

	this.city.isNullOrEmpty() || this.city == "Mountain View" -> {
		LocationError(Exception("定位错误: 不支持当前城市☞ ${this.city}"))
	}

	else -> {
		LocationSuccess(this)
	}
}

class LocationDataSource(private val client: AMapLocationClient) {

	// 一次性监听
	suspend fun getOnceLocation(): AMapLocation {
		return suspendCancellableCoroutine { cont ->
			val callback = AMapLocationListener { location ->
				location ?: return@AMapLocationListener
				cont.resume(location)
			}
			client.setLocationListener(callback)
			client.startLocation()
			cont.invokeOnCancellation { client.unRegisterLocationListener(callback) }
		}
	}

	suspend fun getOnceLocationResult(): LocationResult<AMapLocation> {
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

	companion object {
		@Volatile
		private var INSTANCE: LocationDataSource? = null

		fun getInstance(client: AMapLocationClient): LocationDataSource {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: LocationDataSource(client).also { INSTANCE = it }
			}
		}
	}
}