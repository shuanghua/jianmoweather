package dev.shuanghua.weather.shared

import java.net.UnknownHostException

fun throwAndCastException(e: Exception) {
	when (e) {
		is UnknownHostException -> throw UnknownHostException("站点：请检查网络或者使用中国地区VPN")
		else -> throw e
	}
}