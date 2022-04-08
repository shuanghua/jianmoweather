package jianmoweather.data.repo.location

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.moshuanghua.jianmoweather.shared.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume


//sealed class LocationStatus<out R>
//data class LocationSuccess<T>(val data: T) : LocationStatus<T>()
//data class LocationError(val throwable: Throwable) : LocationStatus<Nothing>()

fun AMapLocation.status(): Result<AMapLocation> = when {
    this.errorCode != 0 -> {
        Result.Error(Exception("定位错误: ${this.errorCode} (${this.locationDetail})"))
    }
    this.city.isNullOrEmpty() || this.city == "Mountain View" -> {
        Result.Error(Exception("定位错误: 不支持当前城市☞ ${this.city}"))
    }
    else -> Result.Success(this)
}


class LocationDataSource(private val client: AMapLocationClient) {

    // 一次性监听
    suspend fun getOnceLocation(): Result<AMapLocation> {
        return suspendCancellableCoroutine { cont ->
            val callback = AMapLocationListener { location ->
                location ?: return@AMapLocationListener
                cont.resume(location.status()) //抛出异常
            }
            client.setLocationListener(callback)
            client.startLocation()
            cont.invokeOnCancellation { client.unRegisterLocationListener(callback) }
        }
    }

    fun observerLocation(): Flow<AMapLocation> = callbackFlow {
        val callback = AMapLocationListener { location ->
            when(val result = location.status()) {
                is Result.Success -> {
                    // sen() 是可挂起函数
                    // trySend()不是可挂起函数，能返回发送的状态，管道满则为false,意味发送失败
                    // trySendBlocking 是一个可以在不同条件下决定是使用 send() 还是使用 trySend() 的一个扩展函数
                    // 如果管道没有满，则使用 trySend() 去发送，如果满了则开启一个协程使用可挂起的 sen() 去发送，当是 sen() 发送失败则抛出异常
                    // trySendBlocking 内部开启了一个runBlocking协程（runBlocking回阻塞当前线程，因此在 runBlocking内的代码是状态安全的），所以千万不要自己再开起一个协程来使用 trySendBlocking , 避免两层协程
                    trySendBlocking(location) // 内部：发送成功：直接return ，发送失败：管道满了或者当前管道被关闭了
                }
                is Result.Error -> cancel(CancellationException("${result.exception.message}"))
            }
        }
        client.setLocationListener(callback)
        awaitClose { client.unRegisterLocationListener(callback) } // 当订阅者停止监听，利用挂起函数 "awaitClose" 来解除订阅
    }

    /**
     * 传入 Lambda
     * 一对多定位监听
     */
    suspend fun observeLocation(observer: suspend (Result<AMapLocation>) -> Unit) = coroutineScope {
        val done = CompletableDeferred<Unit>()
        val callback = object : AMapLocationListener {
            // 笔记：对于大量的业务逻辑的数据读写安全，协程的创建应该在单个线程newSingleThreadContext上创建，
            // 让后将业务逻辑放在协程内执行，这样读写安全且速度快，对应基本数据的读写则建议使用原子类

            // 所谓的安全就是确保数据的完整性和统一性，当一个线程A在写，则应当确保另一个准备读取的线程B 等待 线程A写完 之后再读
            // 而这个等待的机制就叫做 “阻塞” 比如: synchronized 和 ReentrantLock
            // 对于 Coroutine 协程，则使用的时 Mutex 互斥的方案来确保数据安全，他不会阻塞下层的线程， 但更建议在多线程上使用
            val mutex = Mutex() //互斥锁，先到先获得锁
            var observeJob: Job? = null
            override fun onLocationChanged(location: AMapLocation) {
                observeJob?.cancel()
                observeJob = launch {
                    mutex.withLock {
                        //observer(location.status())
                    }
                }
            }
        }
        try {
            client.setLocationListener(callback)
            delay(3000L)
            client.startLocation()//如果设置的定位时间间隔较长，请先添加监听再start, 不然有可能错过第一次回调
            done.await()//确保每一个CallBack都能完成回调
        } finally {
            client.unRegisterLocationListener(callback)
        }
    }

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