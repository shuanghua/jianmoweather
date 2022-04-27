package dev.shuanghua.weather.shared.util

import android.os.SystemClock
import androidx.collection.ArrayMap
import java.util.concurrent.TimeUnit

/**
 * 数据过期验证工具类
 */
class RateLimiter<KEY>(timeout: Long, timeUnit: TimeUnit) {

    private val timestamps = ArrayMap<KEY, Long>()
    private var stationId = ""
    private var _timeout: Long = timeUnit.toMillis(timeout)

    @Synchronized
    fun shouldFetch(key: KEY): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }

        if (now - lastFetched > _timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now(): Long {
        return SystemClock.uptimeMillis()
    }

    @Synchronized
    fun reset(key: KEY) {
        timestamps.remove(key)
    }
}